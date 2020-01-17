package com.ramostear.unaboot.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.jdbc.DBType;
import com.ramostear.unaboot.common.jdbc.UnaBootDataSource;
import com.ramostear.unaboot.common.util.PropertyUtils;
import com.ramostear.unaboot.common.util.UnaBootUtils;
import com.ramostear.unaboot.configuration.support.DruidProperties;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName UnaBootDataSourceConfiguration
 * @Description 自定义数据源配置
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 21:11
 * @Version since UnaBoot-1.0
 **/
@Configuration
public class UnaBootDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.h2")
    public DataSource h2DataSource(DruidProperties properties){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        return properties.druidDataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.mysql")
    @ConditionalOnResource(resources = "classpath:install.back")
    public DataSource mysqlDataSource(DruidProperties properties){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        Properties mySQLProperties = PropertyUtils.read(UnaBootConst.MYSQL_CONFIG_FILE_NAME);
        dataSource.setUrl(mySQLProperties.getProperty(UnaBootConst.MYSQL_URL_KEY));
        dataSource.setUsername(mySQLProperties.getProperty(UnaBootConst.MYSQL_USERNAME_KEY));
        dataSource.setPassword(mySQLProperties.getProperty(UnaBootConst.MYSQL_PASSWORD_KEY));
        return properties.druidDataSource(dataSource);
    }

    @Bean(name = "unaBootDataSource")
    @Primary
    public UnaBootDataSource unaBootDataSource(DataSource h2DataSource,DataSource mysqlDataSource){
        Map<Object,Object> targetDataSource = new HashMap<>(2);
        targetDataSource.put(DBType.H2.name(),h2DataSource);
        targetDataSource.put(DBType.MYSQL.name(),mysqlDataSource);
        if(UnaBootUtils.isInstall()){
            return new UnaBootDataSource(mysqlDataSource,targetDataSource);
        }else{
            return new UnaBootDataSource(h2DataSource,targetDataSource);
        }
    }

    @Bean
    public DruidStatInterceptor druidStatInterceptor(){
        return new DruidStatInterceptor();
    }

    @Bean
    @Scope(scopeName = "prototype")
    public JdkRegexpMethodPointcut jdkRegexpMethodPointcut(){
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPatterns("com.ramostear.unaboot.*");
        return pointcut;
    }

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(DruidStatInterceptor druidStatInterceptor,JdkRegexpMethodPointcut jdkRegexpMethodPointcut){
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
        pointcutAdvisor.setPointcut(jdkRegexpMethodPointcut);
        pointcutAdvisor.setAdvice(druidStatInterceptor);
        return pointcutAdvisor;
    }
}
