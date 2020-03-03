package com.ramostear.unaboot.configuration.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.jdbc.DBType;
import com.ramostear.unaboot.common.util.PropertyUtils;
import com.ramostear.unaboot.common.util.UnaBootUtils;
import com.ramostear.unaboot.configuration.support.DruidProperties;
import com.ramostear.unaboot.configuration.support.H2Property;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * @ClassName UnaBootDataSourceSingleConnectionProviderImpl
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 0:53
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Configuration
public class UnaBootDataSourceSingleConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private static final long serialVersionUID = 2747976319733698776L;

    @Autowired
    private DruidProperties druidProperties;

    private Map<String,DataSource> dataSourceMap = new TreeMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        if(dataSourceMap.isEmpty()){
            if(UnaBootUtils.isInstall()){
                dataSourceMap.put(DBType.MYSQL.name(),wrapper(DBType.MYSQL.name()));
            }
            dataSourceMap.put(DBType.H2.name(),wrapper(DBType.H2.name()));
        }
        return dataSourceMap.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String key) {
        if(!dataSourceMap.containsKey(key)){
            dataSourceMap.put(DBType.H2.name(),wrapper(DBType.H2.name()));
            if(DBType.MYSQL.name().equals(key)){
                if(UnaBootUtils.isInstall()){
                    dataSourceMap.put(key,wrapper(key));
                    return wrapper(key);
                }else{
                    return wrapper(DBType.H2.name());
                }
            }else{
                return wrapper(DBType.H2.name());
            }
        }
        return dataSourceMap.get(key);
    }

    private DruidDataSource wrapper(String key){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        if(DBType.H2.name().equals(key)){
            setH2Property(dataSource);
        }else if(DBType.MYSQL.name().equals(key)){
            setMysqlProperty(dataSource);
        }else{
            setH2Property(dataSource);
        }
        return druidProperties.druidDataSource(dataSource);
    }
    private void setH2Property(DruidDataSource dataSource){
        dataSource.setUrl(H2Property.url);
        dataSource.setUsername(H2Property.username);
        dataSource.setPassword(H2Property.password);
        dataSource.setDriverClassName(H2Property.driverClassName);
    }
    private void setMysqlProperty(DruidDataSource dataSource){
        Properties mySQLProperties = PropertyUtils.read(UnaBootConst.MYSQL_CONFIG_FILE_NAME);
        dataSource.setUrl(mySQLProperties.getProperty(UnaBootConst.MYSQL_URL_KEY));
        dataSource.setUsername(mySQLProperties.getProperty(UnaBootConst.MYSQL_USERNAME_KEY));
        dataSource.setPassword(mySQLProperties.getProperty(UnaBootConst.MYSQL_PASSWORD_KEY));
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    }
}
