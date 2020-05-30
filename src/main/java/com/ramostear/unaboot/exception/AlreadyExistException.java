package com.ramostear.unaboot.exception;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 2:29.
 * The following is the description information about this file:</p>
 * <p>If an object or data exists in the system,please use this exception class,
 * the status code returned is 400.
 * </p>
 */
public class AlreadyExistException extends BadRequestException{
    public AlreadyExistException(String msg) {
        super(msg);
    }
    public AlreadyExistException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
