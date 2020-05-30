package com.ramostear.unaboot.common;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 3:21.
 * The following is the description information about this file:</p>
 * <p>UnaBoot system constants</p>
 */
public class Constants {

    public static final String USER_HOME_KEY = "user.dir";
    /**
     * User home dir path.
     */
    public static String USER_HOME = System.getProperties().getProperty(USER_HOME_KEY);
    /**
     * Path separator.
     */
    public static final String SEPARATOR = File.separator;
    /**
     * File used to mark whether the system is installed.
     */
    public static final String MARK_FILE = ".mark";

    public static final String MYSQL_CONFIG_FILE_NAME = "mysql.properties";

    public static final String MYSQL_URL_KEY = "url";

    public static final String MYSQL_USERNAME_KEY = "username";

    public static final String MYSQL_PASSWORD_KEY = "password";

    public static final String LOGIN_SESSION_KEY = "profile";

    public static final String ALGORITHM_NAME = "MD5";

    public static final int HASH_ITERATIONS = 1024;

    public static final String ENCRYPT_SALT = "1VJOQ78GPCMPFMVD3E0PLDOL5Q";

    public static final DateFormat CRON_DATE_FORMAT = new SimpleDateFormat("ss mm HH dd MM ?");
}
