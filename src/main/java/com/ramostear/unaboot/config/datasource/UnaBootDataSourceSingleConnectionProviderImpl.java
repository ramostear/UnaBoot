package com.ramostear.unaboot.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.config.support.DruidProperties;
import com.ramostear.unaboot.config.support.H2Property;
import com.ramostear.unaboot.common.DataBase;
import com.ramostear.unaboot.util.UnaBootUtils;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 16:58.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Configuration
public class UnaBootDataSourceSingleConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    private static final long serialVersionUID = 5622735918937000949L;

    @Autowired
    private DruidProperties druidProperties;

    private Map<String,DataSource> dataSourceMap = new TreeMap<>();
    @Override
    protected DataSource selectAnyDataSource() {
        if(dataSourceMap.isEmpty()){
            if(UnaBootUtils.isInstalled()){
                dataSourceMap.put(DataBase.MYSQL.name(),wrapper(DataBase.MYSQL.name()));
            }else{
                dataSourceMap.put(DataBase.H2.name(),wrapper(DataBase.H2.name()));
            }
        }
        return dataSourceMap.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String key) {
        if(!dataSourceMap.containsKey(key)){
            dataSourceMap.put(DataBase.H2.name(),wrapper(DataBase.H2.name()));
            if(DataBase.MYSQL.name().equals(key)){
                if(UnaBootUtils.isInstalled()){
                    dataSourceMap.put(key,wrapper(key));
                    return wrapper(key);
                }else{
                    return wrapper(DataBase.H2.name());
                }
            }else{
                return wrapper(DataBase.H2.name());
            }
        }
        return dataSourceMap.get(key);
    }

    private DruidDataSource wrapper(String key){
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        if (key.equals(DataBase.MYSQL.name())) {
            initializeMySQL(dataSource);
        } else {
            initializeH2(dataSource);
        }
        return druidProperties.druidDataSource(dataSource);
    }

    private void initializeMySQL(DruidDataSource dataSource){
        Properties prop = UnaBootUtils.loadPropertyFile(Constants.MYSQL_CONFIG_FILE_NAME);
        assert prop != null;
        dataSource.setUrl(prop.getProperty(Constants.MYSQL_URL_KEY));
        dataSource.setUsername(prop.getProperty(Constants.MYSQL_USERNAME_KEY));
        dataSource.setPassword(prop.getProperty(Constants.MYSQL_PASSWORD_KEY));
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    }

    private void initializeH2(DruidDataSource dataSource){
        dataSource.setUrl(H2Property.url);
        dataSource.setUsername(H2Property.username);
        dataSource.setPassword(H2Property.password);
        dataSource.setDriverClassName(H2Property.driverClassName);
    }
}
