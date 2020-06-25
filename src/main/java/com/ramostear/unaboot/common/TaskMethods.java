package com.ramostear.unaboot.common;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 12:32.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public enum TaskMethods {

    PUBLISH_POST("publishPost","发文计划"),
    REFRESH_INDEX("refreshIndex","索引计划"),
    REMOVE_CACHE("removeCache","缓存计划");

    private String name;
    private String alias;

    TaskMethods(String name,String alias){
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}
