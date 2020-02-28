package com.ramostear.unaboot.common.util;

import com.ibatis.common.jdbc.ScriptRunner;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName ScriptRunnerUtils
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 14:08
 * @Version since UnaBoot-1.0
 **/
@Slf4j
public class ScriptRunnerUtils {

    private  ScriptRunnerUtils(){}

    public static void run(Connection conn,String fileName) throws IOException, SQLException {
        ScriptRunner runner = new ScriptRunner(conn,false,false);
        String fullPath = ScriptRunnerUtils.class.getClassLoader().getResource(fileName).getPath();
        runner.runScript(new FileReader(new File(fullPath)));
    }

    public static void runSql(Connection conn,String sql) throws SQLException {
        log.info("execute sql :{}",sql);
        Statement statement = conn.createStatement();
        statement.executeUpdate(sql);
    }

    public static ResultSet executeQuery(Connection conn,String sql) throws SQLException {
        log.info("execute sql :{}",sql);
        Statement stat = conn.createStatement();
        return stat.executeQuery(sql);
    }

}
