package com.ramostear.unaboot.configuration.support;

import com.ramostear.unaboot.common.jdbc.DBType;
import com.ramostear.unaboot.common.util.UnaBootUtils;

/**
 * @ClassName UnaBootDatasourceContextHolder
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 0:39
 * @Version since UnaBoot-1.0
 **/
public class UnaBootDatasourceContextHolder {
    public static String getIdentifiler(){
        if(UnaBootUtils.isInstall()){
            return DBType.MYSQL.name();
        }else{
            return DBType.H2.name();
        }
    }
}
