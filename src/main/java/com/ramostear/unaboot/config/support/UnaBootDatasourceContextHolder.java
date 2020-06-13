package com.ramostear.unaboot.config.support;

import com.ramostear.unaboot.common.DataBase;
import com.ramostear.unaboot.util.UnaBootUtils;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/13 0013 3:44.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class UnaBootDatasourceContextHolder {
    public static String getIdentifiler(){
        if(UnaBootUtils.isInstalled()){
            return DataBase.MYSQL.name();
        }else{
            return DataBase.H2.name();
        }
    }
}
