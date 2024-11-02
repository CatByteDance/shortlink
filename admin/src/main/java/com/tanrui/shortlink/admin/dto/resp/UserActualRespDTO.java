package com.tanrui.shortlink.admin.dto.resp;

import lombok.Data;



/**
 * 用户返回实体
 */
@Data
public class UserActualRespDTO {
    private Long id;
    private String username;
    private String realName;

    /**
     * 手机号
     */
    private String phone;


    private String mail;

}