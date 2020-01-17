package com.ramostear.unaboot.common.jdbc;

import com.ramostear.unaboot.common.util.UnaBootUtils;

/**
 * @ClassName DataSourceHolder
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:43
 * @Version since UnaBoot-1.0
 **/
public class DataSourceHolder {

    private static final ThreadLocal<String> DATASHOURCE = new ThreadLocal<>();

    public static void setDatashource(String datashource){
        DATASHOURCE.set(datashource);
    }

    public static String getDataSource(){
        if(UnaBootUtils.isInstall()){
            return DBType.MYSQL.name();
        }else{
            return DBType.H2.name();
        }
    }

}
