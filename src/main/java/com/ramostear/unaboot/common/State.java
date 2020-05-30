package com.ramostear.unaboot.common;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/29 0029 20:59.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public enum State {
    OPEN(1,"open"),CLOSE(0,"close");

    private final int code;
    private final String name;

    State(int code,String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
