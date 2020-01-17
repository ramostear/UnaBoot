package com.ramostear.unaboot.common.jdbc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @ClassName UnaBootDataSource
 * @Description 自定义数据源
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:51
 * @Version since UnaBoot-1.0
 **/
public class UnaBootDataSource extends AbstractRoutingDataSource {

    public UnaBootDataSource(DataSource defaultTargetDataSource, Map<Object,Object> targetDataSource){
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSource);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSource();
    }
}
