package com.ramostear.unaboot.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @ClassName UnAuthorizedException
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:39
 * @Version since UnaBoot-1.0
 **/
public class UnAuthorizedException extends UnaBootException {
    public UnAuthorizedException(String message) {
        super(message);
    }

    public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
