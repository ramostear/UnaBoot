package com.ramostear.unaboot.exception;

import org.springframework.http.HttpStatus;

/**
 * @author : ramostear/树下魅狐
 * @version : Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 2:18.
 * The following is the description information about this file:</p>
 * <p>UnaBoot global exception class,other UnaBoot exception classes are inherited from this class.</p>
 */
public abstract class UnaBootException extends RuntimeException{
    // Exception object
    private Object exception;

    public UnaBootException(String msg){
        super(msg);
    }
    public UnaBootException(String msg,Throwable cause){
        super(msg,cause);
    }

    /**
     * Get the status value of the http request.
     * This method is an abstract method and requires subclass implementation.
     * @return      HttpStatus
     */
    public abstract HttpStatus getStatus();

    public UnaBootException setException(Object exception){
        this.exception = exception;
        return this;
    }

    public Object getException() {
        return exception;
    }
}
