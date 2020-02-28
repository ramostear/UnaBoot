package com.ramostear.unaboot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName InstallDto
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 8:43
 * @Version since UnaBoot-1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstallDto {
    private String host;            //数据库主机地址
    private String port;            //数据库端口号
    private String db;              //数据库名称
    private String username;        //数据库登录账户
    private String password;        //数据库登录密码
    private String siteName;        //网站名称
    private String siteDomain;      //网站域名
    private String adminAccount;    //网站管理员账号
    private String adminPassword;   //网站管理员密码
}
