package com.ramostear.unaboot.util;

import com.ibatis.common.jdbc.ScriptRunner;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/12 0012 23:46.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Slf4j
public class SQLUtils {
    private SQLUtils(){}

    public static void run(Connection conn, String fileName) throws IOException, SQLException {
        ScriptRunner runner = new ScriptRunner(conn,false,false);
        String fullPath = SQLUtils.class.getClassLoader().getResource(fileName).getPath();
        runner.runScript(new FileReader(new File(fullPath)));
    }

    public static void runSql(Connection conn,String sql) throws SQLException {
        log.info("execute sql :{}",sql);
        Statement statement = conn.createStatement();
        statement.executeUpdate(sql);
    }

    public static ResultSet executeQuery(Connection conn, String sql) throws SQLException {
        log.info("execute sql :{}",sql);
        Statement stat = conn.createStatement();
        return stat.executeQuery(sql);
    }
}
