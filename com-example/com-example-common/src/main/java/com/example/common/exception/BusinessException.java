package com.example.common.exception;

import lombok.NoArgsConstructor;

/**
 * @Description 业务异常
 * @PackagePath com.example.common.exception.BusinessException
 * @Author YINZHIYU
 * @Date 2020/5/8 13:48
 * @Version 1.0.0.0
 **/
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        {
        }
    }
}
