package com.tanrui.shortlink.project.common.convention.exception;

import com.tanrui.shortlink.project.common.convention.errorcode.BaseErrorCode;
import com.tanrui.shortlink.project.common.convention.errorcode.IErrorCode;

/**
 * 客户端异常
 */
public class ClientException extends AbstractException {

    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ClientException(String message) {
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    public ClientException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    /**
     * 使用一个专门的构造函数（ClientException(String message, Throwable throwable, IErrorCode errorCode)）来调用父类构造函数，
     * 能集中管理如何传递参数给父类。这种方式使得如果需要对父类构造函数的参数逻辑进行修改，只需
     * 要改动一个地方，避免了重复代码。
     */
    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ClientException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
