package com.ramostear.unaboot.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @ClassName BadRequestException
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:36
 * @Version since UnaBoot-1.0
 **/
public class BadRequestException extends UnaBootException{
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
