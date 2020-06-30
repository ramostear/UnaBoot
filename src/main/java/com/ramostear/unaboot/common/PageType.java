package com.ramostear.unaboot.common;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 12:45.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public enum PageType {
    DEFAULT(0),
    CATEGORY(1),
    TAG(2);

    private int code;

    PageType(int code){
        this.code = code;
    }

    public static PageType obtain(int code){
        PageType[] types = PageType.values();
        PageType type = PageType.DEFAULT;
        for (PageType item:types){
            if(item.code == code){
                type = item;
                break;
            }
        }
        return type;
    }
}
