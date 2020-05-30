package com.ramostear.unaboot.sql;

import com.ramostear.unaboot.util.UnaBootUtils;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 3:15.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class DataSourceHolder {

    private static final ThreadLocal<String> DATABASE_CONTEXT = new ThreadLocal<>();

    public static void setDatabaseContext(String databaseName){
        DATABASE_CONTEXT.set(databaseName);
    }

    public static String getCurrentDatasource(){
        if(UnaBootUtils.isInstalled()){
            return DataBase.MYSQL.name();
        }else{
            return DataBase.H2.name();
        }
    }
}
