package com.tanrui.shortlink.admin.common.convention.result;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;




/**
 * 全局返回对象, 所有返回都用该对象
 */
@Data
//允许使用链式调用方式设置属性，比如 result.setCode("0").setMessage("Success")。
@Accessors(chain = true)
public class Result<T> implements Serializable {

//  serialVersionUID用于序列化的唯一标识符，确保在反序列化时版本一致。@Serial用于标识与序列化相关的字段
    @Serial
    private static final long serialVersionUID = 5679018624309023727L;

    /**
     * 正确返回码，常量定义
     */
    public static final String SUCCESS_CODE = "0";

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 请求ID，请求的唯一标识符。
     */
    private String requestId;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }
}