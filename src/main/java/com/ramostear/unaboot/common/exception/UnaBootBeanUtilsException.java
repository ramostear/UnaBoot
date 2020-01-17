package com.ramostear.unaboot.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @ClassName UnaBootBeanUtilsException
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:34
 * @Version since UnaBoot-1.0
 **/
public class UnaBootBeanUtilsException extends UnaBootException {
    public UnaBootBeanUtilsException(String message) {
        super(message);
    }

    public UnaBootBeanUtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
