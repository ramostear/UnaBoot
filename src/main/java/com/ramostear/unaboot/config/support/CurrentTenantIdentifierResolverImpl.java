package com.ramostear.unaboot.config.support;

import com.ramostear.unaboot.sql.DataBase;
import com.ramostear.unaboot.sql.DataSourceHolder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 16:43.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
    @Override
    public String resolveCurrentTenantIdentifier() {
        String identifier = DataSourceHolder.getCurrentDatasource();
        if(StringUtils.isNotBlank(identifier)){
            return identifier;
        }else{
            return DataBase.H2.name();
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
