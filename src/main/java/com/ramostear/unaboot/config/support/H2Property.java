package com.ramostear.unaboot.config.support;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 16:41.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class H2Property {
    private H2Property(){}
    public static String url = "jdbc:h2:mem:test_db";
    public static String username = "sa";
    public static String password = "unaboot123456";
    public static String driverClassName = "org.h2.Driver";
}
