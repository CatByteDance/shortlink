package com.tanrui.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanrui.shortlink.admin.common.convention.exception.ClientException;
import com.tanrui.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.tanrui.shortlink.admin.dao.entity.UserDO;
import com.tanrui.shortlink.admin.dao.mapper.UserMapper;
import com.tanrui.shortlink.admin.dto.req.UserLoginReqDTO;
import com.tanrui.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.tanrui.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.tanrui.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.tanrui.shortlink.admin.dto.resp.UserRespDTO;
import com.tanrui.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.tanrui.shortlink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static com.tanrui.shortlink.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static com.tanrui.shortlink.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public UserRespDTO getUserByUsername(String username) {

//      1.构建查询条件
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
//      2。执行查询
        UserDO userDO = baseMapper.selectOne(queryWrapper);


//      3.创建 `UserRespDTO` 并复制属性
        UserRespDTO result = new UserRespDTO();
        if(userDO != null){
            BeanUtils.copyProperties(userDO, result);       // 此方法需要判空才可以，否则会报错
            return result;
        } else {
//          用户不存在，抛出自定义异常 ClientException
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
//          return null;
        }
    }


    @Override
    public Boolean hasUsername(String username) {
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO reqDTO) {

//      1.先判断用户申请注册的用户名是否存在
        if(!hasUsername(reqDTO.getUsername())){
            throw new ClientException(USER_NAME_EXIST);
        }

        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + reqDTO.getUsername());
        try{
            if(lock.tryLock()){
                //      2.插入用户数据到数据库
                int inserted = baseMapper.insert(BeanUtil.toBean(reqDTO, UserDO.class));
                if(inserted < 1){
                    throw new ClientException(USER_SAVE_ERROR);
                }
                //      3.注册完之后将username加入到布隆过滤器当中
                userRegisterCachePenetrationBloomFilter.add(reqDTO.getUsername());
                return;
            }
            throw new ClientException(USER_NAME_EXIST);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(UserUpdateReqDTO userUpdateReqDTO) {
        // TODO 验证当前用户名是否为登录用户

        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername,userUpdateReqDTO.getUsername());

        baseMapper.update(BeanUtil.toBean(userUpdateReqDTO, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO){
        LambdaQueryWrapper<UserDO> queryWarrper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername,userLoginReqDTO.getUsername())
                .eq(UserDO::getPassword,userLoginReqDTO.getPassword())
                .eq(UserDO::getDelFlag,0);
        UserDO userDO = baseMapper.selectOne(queryWarrper);

        if(userDO == null){
            throw new ClientException("用户不存在");
        }

//      检查用户是否已登录
        Boolean hasLogin = stringRedisTemplate.hasKey("login_" + userLoginReqDTO.getUsername());
        if(hasLogin != null && hasLogin){
            throw new ClientException("用户已登录");
        }

        /**
         * Hash
         * Key: login_用户名
         * Value:
         *   Key: token标识
         *   Val: JSON字符串(用户信息)
         */
//      生成 token 并存储到 Redis
        String uuid = UUID.randomUUID().toString();
//      将登录信息保存到 Redis 中
        stringRedisTemplate.opsForHash().put("login_" + userLoginReqDTO.getUsername(),uuid, JSON.toJSONString(userDO));
//      设置登录状态的过期时间
        stringRedisTemplate.expire("login_" + userLoginReqDTO.getUsername(), 30L, TimeUnit.MINUTES);

        return new UserLoginRespDTO(uuid);
    }


    @Override
    public Boolean checkLogin(String username, String token) {
        return stringRedisTemplate.opsForHash().get("login_" + username, token) != null;
    }

    @Override
    public void logout(String username, String token) {
        if(checkLogin(username, token)){
            stringRedisTemplate.delete("login_" + username);
            return;
        }
        throw new ClientException("用户token不存在或用户未登录");
    }
}
