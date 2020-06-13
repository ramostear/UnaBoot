package com.ramostear.unaboot.config.support;

import com.ramostear.unaboot.common.DataBase;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 16:43.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class CurrentUnaBootDataSourceIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
    private static final String DEFAULT_DATASOURCE_IDENTIFIER = DataBase.H2.name();

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
