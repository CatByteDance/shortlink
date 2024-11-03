package com.tanrui.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 更新用户信息
 */
@Data
public class UserUpdateReqDTO {
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String mail;
}
