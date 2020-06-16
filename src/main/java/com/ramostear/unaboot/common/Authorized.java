package com.ramostear.unaboot.common;

import org.springframework.util.StringUtils;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 5:22.
 * The following is the description information about this file:</p>
 * <p>Authorize names</p>
 */
public enum Authorized {
    ADMIN("admin","管理员"),
    EDITOR("editor","小编"),
    MEMBER("member","会员"),
    GUEST("guest","来宾");

    private final String name;
    private final String alias;

    Authorized(String name,String alias){
        this.name = name;
        this.alias = alias;
    }

    public String getName(){
        return this.name;
    }

    public String getAlias() {
        return alias;
    }

    public Authorized value(String name){
        if(StringUtils.isEmpty(name)){
            return null;
        }
        Authorized auth = null;
        for(Authorized authorized : Authorized.values()){
            if(authorized.getName().equals(name)){
                auth = authorized;
            }
        }
        if(auth == null){
            throw new IllegalArgumentException("No matching constant for [" + name + "]");
        }
        return auth;

    }
}
