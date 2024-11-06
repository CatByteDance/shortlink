package com.tanrui.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanrui.shortlink.admin.dao.entity.UserDO;
import com.tanrui.shortlink.admin.dto.req.UserLoginReqDTO;
import com.tanrui.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.tanrui.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.tanrui.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.tanrui.shortlink.admin.dto.resp.UserRespDTO;

/**
 * 用户接口层
 */
public interface UserService extends IService <UserDO> {

    /**
     * 根据用户名查找用户信息
     * @param username 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String username);


    /**
     * 查询用户名是否存在
     * @param username 用户名
     * @return 用户名存在返回true, 不存在返回false
     */
    Boolean hasUsername(String username);

    /**
     * 注册用户
     * @param userRegisterReqDTO 注册用户请求参数
     */
    void register(UserRegisterReqDTO userRegisterReqDTO);

    /**
     * 根据用户名更新用户
     * @param userUpdateReqDTO 更新用户请求参数
     */
    void update(UserUpdateReqDTO userUpdateReqDTO);

    /**
     * 用户登录
     * @param userLoginReqDTO 用户登录请求参数
     * @return RespDTO 包含 token 的响应对象
     */
    UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO);

    /**
     * 检查用户是否登录
     * @param username 用户名
     * @param token 用户登录Token
     * @return 用户是否登录标识
     */
    Boolean checkLogin(String username, String token);

    /**
     * 退出登录
     * @param username 用户名
     * @param token 用户登录token
     */
    void logout(String username, String token);
}
