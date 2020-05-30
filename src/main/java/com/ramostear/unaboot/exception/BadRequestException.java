package com.ramostear.unaboot.exception;

import org.springframework.http.HttpStatus;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 2:30.
 * The following is the description information about this file:</p>
 * <p>If the status code of http request is 400,please use this exception class.</p>
 */
public class BadRequestException extends UnaBootException {


    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
