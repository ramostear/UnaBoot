package com.ramostear.unaboot.configuration.support;

import com.ramostear.unaboot.common.jdbc.DBType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * @ClassName CurrentUnaBootDataSourceIdentifierResolverImpl
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 0:36
 * @Version since UnaBoot-1.0
 **/
public class CurrentUnaBootDataSourceIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private static final String DEFAULT_DATASOURCE_IDENTIFIER = DBType.H2.name();

    @Override
    public String resolveCurrentTenantIdentifier() {
        String identifier = UnaBootDatasourceContextHolder.getIdentifiler();
        return StringUtils.isNotBlank(identifier)?identifier:DEFAULT_DATASOURCE_IDENTIFIER;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
