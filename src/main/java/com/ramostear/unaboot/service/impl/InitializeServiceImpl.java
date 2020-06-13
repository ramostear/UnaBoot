package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.Authorized;
import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.common.Locked;
import com.ramostear.unaboot.common.State;
import com.ramostear.unaboot.domain.dto.InitializeDto;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.service.InitializeService;
import com.ramostear.unaboot.config.support.DataBaseProperty;
import com.ramostear.unaboot.util.DateTimeUtils;
import com.ramostear.unaboot.util.SQLUtils;
import com.ramostear.unaboot.util.UnaBootUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/12 0012 23:32.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("initializeService")
public class InitializeServiceImpl implements InitializeService {
    private static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void configurer(InitializeDto initializeDto) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException, IOException {
        DataBaseProperty property = getDataBaseProperty(initializeDto);
        Class.forName(property.getDriverClassName()).newInstance();
        Connection connection = DriverManager.getConnection(property.getConnectionUrl(),property.getUsername(),property.getPassword());
        createAndUseDatabaseIfAbsent(connection,property);
        SQLUtils.run(connection, Constants.INITIAL_SCHEMA_FILE);
        initializeDatabase(connection,initializeDto);
        writeProperties(property);
        UnaBootUtils.marked();
    }

    private DataBaseProperty getDataBaseProperty(InitializeDto data){
        DataBaseProperty property = new DataBaseProperty();
        property.setPassword(data.getPassword());
        property.setUsername(data.getUsername());
        property.setPort(data.getPort());
        property.setHost(data.getHost());
        property.setDatabase(data.getDb());
        property.setDriverClassName("com.mysql.jdbc.Driver");
        return property;
    }

    private void createAndUseDatabaseIfAbsent(Connection connection,DataBaseProperty property) throws SQLException {
        String createDB = "CREATE DATABASE IF NOT EXISTS `"+property.getDatabase()+"` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;";
        SQLUtils.runSql(connection,createDB);
        String useDB = "USE `"+property.getDatabase()+"` ;";
        SQLUtils.runSql(connection,useDB);
    }

    private void initializeDatabase(Connection connection,InitializeDto data) throws SQLException {
        String userSql = initUser(data);
        SQLUtils.runSql(connection,userSql);
        String settingsSql = initSettings(data);
        SQLUtils.runSql(connection,settingsSql);
    }

    private String initUser(InitializeDto data){
        User user = new User();
        user.setUsername(data.getAdminAccount());
        user.setEmail(data.getAdminEmail());
        user.setState(State.OPEN.getCode());
        user.setLocked(Locked.YES.getCode());
        Date now = DateTimeUtils.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setPassword(UnaBootUtils.simpleHash(data.getAdminPassword(),user.getUsername()));
        user.setNickname("Admin");
        user.setRole(Authorized.ADMIN.getName());
        return "INSERT INTO `USER`(`ID`,`CREATE_TIME`,`UPDATE_TIME`,`NICKNAME`,`PASSWORD`,`ROLE`,`USERNAME`,`EMAIL`,`STATE`,`LOCKED`) values (" +
                1 + ",'" + FORMAT.format(user.getCreateTime())+"','"+FORMAT.format(user.getUpdateTime())+"'," +
                "'" + user.getNickname() + "','"+user.getPassword() + "','"+user.getRole()+"'," +
                "'" + user.getUsername() +"','" + user.getEmail() + "',"+user.getState() + ","  + user.getLocked() + ");";
    }

    private String initSettings(InitializeDto data){
        String datetime = FORMAT.format(DateTimeUtils.now());
        return "insert  into `settings`(`id`,`create_time`,`update_time`,`_key`,`_value`) values " +
                "(1,'"+datetime+"','"+datetime+"','title','"+data.getTitle()+"')," +
                "(2,'"+datetime+"','"+datetime+"','domain','"+data.getDomain()+"')," +
                "(3,'"+datetime+"','"+datetime+"','theme','default');";
    }

    private void writeProperties(DataBaseProperty property){
        Map<String,String> map = new HashMap<>();
        map.put("url",property.getConnectionUrl());
        map.put("username",property.getUsername());
        map.put("password",property.getPassword());
        UnaBootUtils.setPropertyFile(Constants.MYSQL_CONFIG_FILE_NAME,map);
    }
}
