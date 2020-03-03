package com.ramostear.unaboot.configuration.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName H2Property
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 2:13
 * @Version since UnaBoot-1.0
 **/
public class H2Property {
    private H2Property(){}
    public static String url = "jdbc:h2:mem:test_db";
    public static String username = "sa";
    public static String password = "123456";
    public static String driverClassName = "org.h2.Driver";
}
