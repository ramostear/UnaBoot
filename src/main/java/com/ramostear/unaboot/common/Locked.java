package com.ramostear.unaboot.common;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/13 0013 0:20.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public enum Locked {
    YES(-1),NO(0);

    private int code;

    Locked(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
