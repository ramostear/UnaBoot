package com.ramostear.unaboot.exception;

import org.springframework.http.HttpStatus;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 2:41.
 * The following is the description information about this file:</p>
 * <p>If you refuse to respond to the user`s request for the target resource,
 * please use this exception class,and the returned http status code is 403.</p>
 */
public class ForbiddenException extends UnaBootException {
    public ForbiddenException(String msg) {
        super(msg);
    }

    public ForbiddenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
