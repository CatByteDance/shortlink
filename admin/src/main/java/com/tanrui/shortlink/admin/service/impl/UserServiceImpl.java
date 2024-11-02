package com.tanrui.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanrui.shortlink.admin.common.convention.exception.ClientException;
import com.tanrui.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.tanrui.shortlink.admin.dao.entity.UserDO;
import com.tanrui.shortlink.admin.dao.mapper.UserMapper;
import com.tanrui.shortlink.admin.dto.resp.UserRespDTO;
import com.tanrui.shortlink.admin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现层
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

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

//      1.构建查询条件
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
//      2。执行查询
        UserDO userDO = baseMapper.selectOne(queryWrapper);

//      3.userDO存在返回true, 不存在返回False
        return userDO != null;
    }
}
