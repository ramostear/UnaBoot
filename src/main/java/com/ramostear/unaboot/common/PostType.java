package com.ramostear.unaboot.common;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 15:55.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public enum PostType {

    DEFAULT(0),
    PREVIOUS(1),
    NEXT(2);

    private int code;

    PostType(int code){
        this.code = code;
    }

    public static PostType obtain(int code){
        PostType[] types = PostType.values();
        PostType type = PostType.DEFAULT;
        for (PostType item : types){
            if(item.code == code){
                type = item;
                break;
            }
        }
        return type;
    }
}
