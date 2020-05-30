package com.ramostear.unaboot.sql;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 3:09.
 * The following is the description information about this file:</p>
 * <p>Custom dynamic datasource and inherited from AbstractRoutingDatasource.
 * Used to realize the dynamic switching of datasource in the system.</p>
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    public DynamicDataSource(DataSource defaultTargetDatasource, Map<Object,Object> targetDatasources){
        super.setDefaultTargetDataSource(defaultTargetDatasource);
        super.setTargetDataSources(targetDatasources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getCurrentDatasource();
    }
}
