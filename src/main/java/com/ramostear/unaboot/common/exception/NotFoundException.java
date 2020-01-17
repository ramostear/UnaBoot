package com.ramostear.unaboot.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @ClassName NotFoundException
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:37
 * @Version since UnaBoot-1.0
 **/
public class NotFoundException extends UnaBootException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
