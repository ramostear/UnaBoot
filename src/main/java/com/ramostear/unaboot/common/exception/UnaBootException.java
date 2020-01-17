package com.ramostear.unaboot.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * @ClassName UnaBootException
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:30
 * @Version since UnaBoot-1.0
 **/
public abstract class UnaBootException extends RuntimeException{

    private Object exception;

    public UnaBootException(String message){
        super(message);
    }

    public UnaBootException(String message,Throwable cause){
        super(message, cause);
    }

    public abstract HttpStatus getStatus();

    public Object getException(){
        return exception;
    }

    public UnaBootException setException(@Nullable Object exception){
        this.exception = exception;
        return this;
    }
}
