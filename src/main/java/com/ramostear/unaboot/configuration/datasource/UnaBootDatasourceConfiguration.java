package com.ramostear.unaboot.configuration.datasource;

import com.ramostear.unaboot.configuration.support.CurrentUnaBootDataSourceIdentifierResolverImpl;
import com.ramostear.unaboot.repository.support.UnaBootRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
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
 * @ClassName UnaBootDatasourceConfiguration
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 1:26
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.ramostear.unaboot"})
@EnableJpaRepositories(basePackages = {"com.ramostear.unaboot"},
                       entityManagerFactoryRef = "unaBootEntityManagerFactory",
                       transactionManagerRef = "unaBootTransactionManager",
                       repositoryBaseClass = UnaBootRepositoryImpl.class)
public class UnaBootDatasourceConfiguration {

    @Bean("jpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter(){
        return new HibernateJpaVendorAdapter();
    }

    @Bean(name = "unaBootTransactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    @Bean(name = "unaBootDataSourceSingleConnectionProvider")
    public MultiTenantConnectionProvider multiTenantConnectionProvider(){
        return new UnaBootDataSourceSingleConnectionProviderImpl();
    }
    @Bean(name = "unaBootIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver(){
        return new CurrentUnaBootDataSourceIdentifierResolverImpl();
    }

    @Bean(name ="unaBootEntityManagerFactory")
    @ConditionalOnBean(name = "unaBootDataSourceSingleConnectionProvider")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("unaBootDataSourceSingleConnectionProvider")MultiTenantConnectionProvider connectionProvider,
                                                                       @Qualifier("unaBootIdentifierResolver")CurrentTenantIdentifierResolver tenantIdentifierResolver){
        LocalContainerEntityManagerFactoryBean local = new LocalContainerEntityManagerFactoryBean();
        local.setPackagesToScan("com.ramostear.unaboot");
        local.setJpaVendorAdapter(jpaVendorAdapter());
        local.setPersistenceUnitName("una-boot-persistence-unit");
        Map<String,Object> props = new HashMap<>();
        props.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        props.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER,connectionProvider);
        props.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,tenantIdentifierResolver);
        props.put(Environment.DIALECT,"org.hibernate.dialect.MySQL5Dialect");
        props.put(Environment.SHOW_SQL,true);
        props.put(Environment.FORMAT_SQL,true);
        props.put(Environment.HBM2DDL_AUTO,"none");
        local.setJpaPropertyMap(props);
        return local;
    }
}
