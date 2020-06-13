package com.ramostear.unaboot.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ramostear.unaboot.config.support.DruidProperties;
import com.ramostear.unaboot.config.support.H2Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 16:37.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Configuration
public class DefaultUnaBootDatasourceConfiguration {

    @Autowired
    private DruidProperties druidProperties;

    @Bean
    public DataSource defaultDataSource(){
        DruidDataSource defaultDataSource = DruidDataSourceBuilder.create().build();
        defaultDataSource.setUrl(H2Property.url);
        defaultDataSource.setDriverClassName(H2Property.driverClassName);
        defaultDataSource.setUsername(H2Property.username);
        defaultDataSource.setPassword(H2Property.password);
        return druidProperties.druidDataSource(defaultDataSource);
    }
}
