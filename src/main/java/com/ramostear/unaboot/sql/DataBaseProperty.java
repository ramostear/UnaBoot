package com.ramostear.unaboot.sql;

import lombok.Data;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 2:54.
 * The following is the description information about this file:</p>
 * <p>This class wraps common properties of database connections.</p>
 */
@Data
public class DataBaseProperty {
    private String host = "localhost";
    private String database = "UnaBoot";
    private String username = "root";
    private String password = "root";
    private String port = "3306";
    private String driverClassName = "com.mysql.jdbc.Driver";

    /**
     *  Get database connection address.The default values are as follows:
     *  host:localhost</br>
     *  database:UnaBoot</br>
     *  username:root</br>
     *  password:root</br>
     *  port:3306</br>
     *  driverClassName:com.mysql.jdbc.Driver</br>
     *  url:jdbc:mysql://localhost:3306/UnaBoot?useUnicode=true&useSSL=false&characterEncoding=utf8.
     * @return      Database connection url.
     */
    public String getConnectionUrl(){
        StringBuilder url = new StringBuilder("jdbc:mysql://");
        url.append(host)
          .append(":")
          .append(port)
          .append("/")
          .append(database)
          .append("?useUnicode=true&useSSL=false&characterEncoding=utf8");
        return url.toString();
    }
}
