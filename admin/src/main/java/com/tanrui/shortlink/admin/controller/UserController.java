package com.tanrui.shortlink.admin.controller;


import com.tanrui.shortlink.admin.dto.resp.UserRespDTO;
import com.tanrui.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制层
 */

//表示这个类中的方法会自动将返回的值作为 HTTP 响应的内容，而不是渲染成视图。
@RestController
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
//  映射 HTTP GET 请求。URL 中的 {username} 是一个 路径变量，它代表用户输入的动态部分。
//  PathVariable将 URL 中的路径变量 {username} 映射到方法参数 username 上
    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/shortlink/v1/user/{username}")
    public UserRespDTO getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }
}

