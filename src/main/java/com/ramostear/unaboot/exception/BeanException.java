package com.ramostear.unaboot.exception;

import org.springframework.http.HttpStatus;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 2:49.
 * The following is the description information about this file:</p>
 * <p>An exception occurs when converting between beans,please use this exception class
 * and http status code is 500.</p>
 */
public class BeanException extends UnaBootException {
    public BeanException(String msg) {
        super(msg);
    }

    public BeanException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
