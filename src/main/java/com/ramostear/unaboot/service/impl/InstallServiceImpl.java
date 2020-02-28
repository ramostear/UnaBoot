package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.jdbc.DBInfo;
import com.ramostear.unaboot.common.util.*;
import com.ramostear.unaboot.domain.dto.InstallDto;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.service.InstallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName InstallServiceImpl
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 14:05
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service
public class InstallServiceImpl implements InstallService {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean install(InstallDto dto) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException {
        DBInfo info = initInfo(dto);
        Class.forName(info.getDriverName()).newInstance();
        Connection conn = DriverManager.getConnection(info.getUrl(),info.getUsername(),info.getPassword());
        String createDatabaseSql = "CREATE DATABASE IF NOT EXISTS `"+info.getDb()+"` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;";
        String useDatabaseSql = "USE `"+info.getDb()+"`;";
        ScriptRunnerUtils.runSql(conn,createDatabaseSql);
        log.info("create database {} success",info.getDb());
        ScriptRunnerUtils.runSql(conn,useDatabaseSql);
        log.info("use database `{}` success",info.getDb());
        ScriptRunnerUtils.run(conn,"unabootschema.sql");
        log.info("create tables into database `{}` is ok.",info.getDb());
        ScriptRunnerUtils.runSql(conn,initUserSql(dto));
        ScriptRunnerUtils.runSql(conn,initSettingSql(dto));
        UnaBootUtils.writeInstallFile();
        Map<String,String> props = new HashMap<>();
        props.put("url",info.getUrl());
        props.put("username",info.getUsername());
        props.put("password",info.getPassword());
        PropertyUtils.write("mysql.properties",props);
        return true;
    }



    private DBInfo initInfo(InstallDto dto){
        DBInfo info = new DBInfo();
        info.setPassword(dto.getPassword());
        info.setUsername(dto.getUsername());
        info.setPort(dto.getPort());
        info.setHost(dto.getHost());
        info.setDb(dto.getDb());
        info.setDriverName("com.mysql.jdbc.Driver");
        return info;
    }
    private String initUserSql(InstallDto dto){
        User user = new User();
        user.setUpdateTime(DateTimeUtils.current());
        user.setNickname("Admin");
        user.setUsername(dto.getAdminAccount());
        user.setRole(UnaBootConst.ROLE_ADMIN);
        user.setType(UnaBootConst.USER_DEFAULT);
        user.setCreateTime(DateTimeUtils.current());
        user.setPassword(EncryptUtils.simpleHash(dto.getAdminPassword(),dto.getAdminAccount()));
        String sql = "insert into `user`(`id`,`create_time`,`update_time`,`nickname`,`password`,`role`,`username`,`type`) values ("+1
                +",'"+sdf.format(user.getCreateTime())+"','"+sdf.format(user.getUpdateTime())
                +"','"+user.getNickname()+"','"+user.getPassword()+"','"+user.getRole()
                +"','"+user.getUsername()+"',"+user.getType()+");";

        return sql;
    }

    private String initSettingSql(InstallDto dto){
        String dateTime = sdf.format(DateTimeUtils.current());
        String sql = "insert  into `settings`(`id`,`create_time`,`update_time`,`_key`,`_value`) values (1,'2020-02-15 18:36:56','2020-02-15 18:36:56','title','"+dto.getSiteName()+"'),(2,'2020-02-15 18:36:56','2020-02-15 18:36:56','domain','"+dto.getSiteDomain()+"');";
        return sql;
    }
}
