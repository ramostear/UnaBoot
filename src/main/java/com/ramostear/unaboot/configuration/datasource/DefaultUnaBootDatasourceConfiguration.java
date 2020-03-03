package com.ramostear.unaboot.configuration.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ramostear.unaboot.configuration.support.DruidProperties;
import com.ramostear.unaboot.configuration.support.H2Property;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @ClassName DefaultUnaBootDatasourceConfiguration
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/3/2 0002 23:40
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Configuration
public class DefaultUnaBootDatasourceConfiguration {
    @Autowired
    private DruidProperties druidProperties;

    @Bean
    public DataSource defaultUnaBootDatasource(){
        log.info("Start to configuration defaultUnaBootDatasource");
        DruidDataSource defaultUnaBootDatasource = DruidDataSourceBuilder.create().build();
        defaultUnaBootDatasource.setUrl(H2Property.url);
        defaultUnaBootDatasource.setDriverClassName(H2Property.driverClassName);
        defaultUnaBootDatasource.setUsername(H2Property.username);
        defaultUnaBootDatasource.setPassword(H2Property.password);
        log.info("Configuration defaultUnaBootDatasource successfully!");
        return druidProperties.druidDataSource(defaultUnaBootDatasource);
    }
}
