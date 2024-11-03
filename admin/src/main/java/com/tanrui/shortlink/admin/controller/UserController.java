package com.tanrui.shortlink.admin.controller;


import cn.hutool.core.bean.BeanUtil;
import com.tanrui.shortlink.admin.common.convention.result.Result;
import com.tanrui.shortlink.admin.common.convention.result.Results;
import com.tanrui.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.tanrui.shortlink.admin.dto.resp.UserActualRespDTO;
import com.tanrui.shortlink.admin.dto.resp.UserRespDTO;
import com.tanrui.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/api/short-link/v1/user/{username}")
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

        /**
         * 根据用户名查询无脱敏用户信息
         */
    @GetMapping("/api/short-link/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
        /**
         * !!! 转换为非脱敏 DTO
         * BeanUtil.toBean 会将 UserRespDTO 中的数据（包括 phone 字段的原始值）转换为 UserActualRespDTO 对象。
         * UserActualRespDTO 中的 phone 字段没有 @JsonSerialize 注解，因此 Jackson 在序列化为 JSON 时不会进行脱敏处理。
         * PS: 序列化为 JSON是最后一步，所以提前转为UserActualRespDTO对象即可
         */
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class));
    }


    /**
     * 查询用户名是否存在
     * @RequestParam ("username"从请求中获取 username 参数，将其作为方法的输入。即客户端在调用接口时需要提供 username 参数，例如 ?username=johndoe。
     */
    @GetMapping("/api/short-link/v1/user/has-username")
    public Result<Boolean> hasUser(@RequestParam ("username") String username) {
        return Results.success(userService.hasUsername(username));
    }

    /**
     * 注册用户
     */
    @PostMapping("/api/short-link/v1/user")
    public Result<Void> resgister(@RequestBody UserRegisterReqDTO reqDTO) {
        userService.register(reqDTO);
        return Results.success();
    }
}

