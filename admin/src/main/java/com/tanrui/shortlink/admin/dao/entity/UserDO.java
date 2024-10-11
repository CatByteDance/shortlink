package com.tanrui.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户持久层实体
 * 1.通过 @Data 注解，避免了手动编写 getter、setter、toString、equals、hashCode 等方法，从而简化了代码结构，提升了开发效率
 *   特别适合那些只包含数据且不需要复杂逻辑的类（例如 DTO、实体类等）
 *
 */
@Data
@TableName("t_user")
public class UserDO {

    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String mail;
    private Long deletionTime;
    private Date createTime;
    private Date updateTime;
    private Integer delFlag;

}
