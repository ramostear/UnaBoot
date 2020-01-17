package com.ramostear.unaboot.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @ClassName ForbiddenException
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:39
 * @Version since UnaBoot-1.0
 **/
public class ForbiddenException extends UnaBootException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
