package com.ramostear.unaboot.exception;

import org.springframework.http.HttpStatus;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 2:45.
 * The following is the description information about this file:</p>
 * <p>Whe users request unauthorized resources,please use this exception class,
 * the http status code returned by this exception class is 401.</p>
 */
public class UnAuthorizedException extends UnaBootException {

    public UnAuthorizedException(String msg) {
        super(msg);
    }

    public UnAuthorizedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
