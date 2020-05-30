package com.ramostear.unaboot.exception;

import org.springframework.http.HttpStatus;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 2:37.
 * The following is the description information about this file:</p>
 * <p>If the resource does not exist or is not found,please use this exception class,
 * the returned http status code is 404.
 * </p>
 */
public class NotFoundException extends UnaBootException {
    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
