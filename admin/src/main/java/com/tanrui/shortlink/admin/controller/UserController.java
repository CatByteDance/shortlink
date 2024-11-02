package com.tanrui.shortlink.admin.controller;


import com.tanrui.shortlink.admin.common.convention.result.Result;
import com.tanrui.shortlink.admin.common.convention.result.Results;
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
    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/shortlink/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        /**
         *     开发全局异常拦截器之后不需要, 直接return success即可，因为userRespDTO为null的话，
         *     已经在UserServiceImpl中抛出ClientException(UserErrorCodeEnum.USER_NULL)，被全局拦截器拦截了，所以不需要进行判断
         *     userRespDTO是否为null，如果进行到这里的话，userRespDTO必不为空
         *
         *     UserRespDTO userRespDTO = userService.getUserByUsername(username);
         *     if (userRespDTO == null) {
         *         return new Result<UserRespDTO>().setCode(UserErrorCodeEnum.USER_NULL.code()).setMessage(UserErrorCodeEnum.USER_NULL.message());
         *     }
         *         return Results.success(userRespDTO);
         * }
         */
        return Results.success(userService.getUserByUsername(username));
    }
}

