package com.ramostear.unaboot.common;

/**
 * @author :    ramostear/树下魅狐
 * @version :   Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 13:42.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public enum GitalkKeys {

    ENABLED("gitalk_enabled"),
    CLIENT_ID("gitalk_client_id"),
    CLIENT_SECRET("gitalk_client_secret"),
    REPO("gitalk_repo"),
    OWNER("gitalk_owner"),
    ADMIN("gitalk_admin");

    private String key;

    GitalkKeys(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
