package com.ramostear.unaboot.common.exception;

/**
 * @ClassName AlreadyExistException
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:35
 * @Version since UnaBoot-1.0
 **/
public class AlreadyExistException extends BadRequestException {

    public AlreadyExistException(String message) {
        super(message);
    }

    public AlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
