package com.ramostear.unaboot.common;

import java.io.File;

/**
 * @ClassName UnaBootConst
 * @Description UnaBoot系统常量
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 19:27
 * @Version since UnaBoot-1.0
 **/
public class UnaBootConst {

    public static final String USER_HOME_KEY = "user.home";
    /**
     * UnaBoot用于获取主机用户的home目录
     */
    public static final String USER_HOME = System.getProperties().getProperty(UnaBootConst.USER_HOME_KEY);

    /**
     * 路径分割符
     */
    public static final String SEPARATOR = File.separator;

    public static final String FILE_UPLOAD_ROOT_DIR =
            UnaBootConst.USER_HOME
            +SEPARATOR+".una"
            +SEPARATOR;
    /**
     * 系统版本
     */
    public static final String VERSION = "1.1.0";

    /**
     * 自定义FreeMarker指令名称前缀
     */
    public static final String DIRECTIVE_PREFIX = "u_";
    /**
     * UTF-8 Encoding name
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * ISO-8859-1 Encoding name
     */
    public static final String ISO_8859_1 = "ISO-8859-1";
    /**
     * GBK Encoding
     */
    public static final String GBK = "GBK";

    /**
     * GBK-2312 Encoding
     */
    public static final String GBK_2312 = "GBK-2312";
    /**
     * img标签正则表达式
     */
    public static final String IMG_PATTERN = "(?i)(\\<img)([^\\>]+\\>)";
    /**
     * img标签中src属性正则表达式
     */
    public static final String IMG_SRC_PATTERN ="<(img|IMG)(.*?)(/>|></img>|>)";
    /**
     * img标签中src值的正则表达式
     */
    public static final String IMG_SRC_VAL_PATTERN = "(src|SRC)=(\"|\')(.*?)(\"|\')";
    /**
     * 超级管理员角色
     */
    public static final String ROLE_ADMIN = "administrator";

    /**
     * 默认时间格式化模板
     */
    public static final String FULL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 默认的加密盐
     */
    public static final String ENCRYPT_SALT = "1VJOQ78GPCMPFMVD3E0PLDOL5Q";
    /**
     * 标记已安装的文件名称
     */
    public static final String INSTALL_FLAG_FILE = "install.back";
    /**
     * MySQL数据库配置文件名称
     */
    public static final String MYSQL_CONFIG_FILE_NAME = "mysql.properties";

    public static final String MYSQL_URL_KEY = "url";

    public static final String MYSQL_USERNAME_KEY = "username";

    public static final String MYSQL_PASSWORD_KEY = "password";

    public static final String ELLIPSIS_VALUE = "...";

    public static final String EMPTY_CHARACTER = "";

    public static final String ALGORITHM_NAME = "MD5";
    public static final int HASH_ITERATIONS = 1024;

    public static final int DRAFT = 0;
    public static final int ACTIVE = 1;
    public static final int WAIT = -1;

    public static final int USER_NEW = 0;
    public static final int USER_DEFAULT = 1;

}
