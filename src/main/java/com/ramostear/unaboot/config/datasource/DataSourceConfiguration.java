package com.ramostear.unaboot.config.datasource;

import com.ramostear.unaboot.config.support.CurrentTenantIdentifierResolverImpl;
import com.ramostear.unaboot.repository.impl.BaseRepositoryImpl;
import com.ramostear.unaboot.util.UnaBootUtils;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 16:51.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.ramostear.unaboot"})
@EnableJpaRepositories(basePackages = {"com.ramostear.unaboot"},
entityManagerFactoryRef = "unaBootEntityManagerFactory",transactionManagerRef = "unaBootTransactionManager",
        repositoryBaseClass = BaseRepositoryImpl.class
)
public class DataSourceConfiguration {

    @Bean("jpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter(){
        return new HibernateJpaVendorAdapter();
    }

    @Bean(name="unaBootTransactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    @Bean(name = "dataSourceSingleConnectionProvider")
    public MultiTenantConnectionProvider multiTenantConnectionProvider(){
        return new DataSourceSingleConnectionProviderImpl();
    }

    @Bean(name = "currentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver(){
        return new CurrentTenantIdentifierResolverImpl();
    }

    @Bean(name = "unaBootEntityManagerFactory")
    @ConditionalOnBean(name = "dataSourceSingleConnectionProvider")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(@Qualifier("dataSourceSingleConnectionProvider") MultiTenantConnectionProvider multiTenantConnectionProvider,
                                                                           @Qualifier("currentTenantIdentifierResolver")CurrentTenantIdentifierResolver currentTenantIdentifierResolver){
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPackagesToScan("com.ramostear.unaboot");
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactory.setPersistenceUnitName("unaboot-persistence-unit");
        Map<String,Object> prop = new HashMap<>();
        prop.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        prop.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER,multiTenantConnectionProvider);
        prop.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,currentTenantIdentifierResolver);
        if(UnaBootUtils.isInstalled()){
            prop.put(Environment.DIALECT,"org.hibernate.dialect.MySQL5Dialect");
            prop.put(Environment.HBM2DDL_AUTO,"update");
        }else{
            prop.put(Environment.DIALECT,"org.hibernate.dialect.H2Dialect");
            prop.put(Environment.HBM2DDL_AUTO,"none");
        }
        prop.put(Environment.SHOW_SQL,true);
        prop.put(Environment.FORMAT_SQL,true);
        entityManagerFactory.setJpaPropertyMap(prop);
        return entityManagerFactory;
    }
}
