package com.ramostear.unaboot.common.jdbc;

import lombok.Data;

/**
 * @ClassName DBInfo
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 7:40
 * @Version since UnaBoot-1.0
 **/
@Data
public class DBInfo {

    private String host;
    private String db;
    private String username;
    private String password;
    private String port;
    private String driverName;
    private String url;

    public String getUrl(){
        return "jdbc:mysql://"+host+":"+port+"/"+db+"?useUnicode=true&useSSL=false&characterEncoding=utf8";
    }
}
