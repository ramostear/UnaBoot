package com.ramostear.unaboot.configuration;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.configuration.support.UnaBootAuthorizingRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName ShiroConfiguration
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/28 0028 6:54
 * @Version since UnaBoot-1.0
 **/
@Configuration
public class ShiroConfiguration {
    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache-shiro.xml");
        return ehCacheManager;
    }
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        return factoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(securityManager);
        return sourceAdvisor;
    }

    @Bean
    public UnaBootAuthorizingRealm unaBootAuthorizingRealm(){
        UnaBootAuthorizingRealm realm = new UnaBootAuthorizingRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        realm.setCachingEnabled(true);
        realm.setCacheManager(ehCacheManager());
        return realm;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(UnaBootConst.ALGORITHM_NAME);
        credentialsMatcher.setHashIterations(UnaBootConst.HASH_ITERATIONS);
        return credentialsMatcher;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setRememberMeManager(cookieRememberMeManager());
        securityManager.setRealm(unaBootAuthorizingRealm());
        return securityManager;
    }
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty("UnauthorizedException","/admin/login");
        resolver.setExceptionMappings(properties);
        return resolver;
    }

    @Bean
    public SimpleCookie simpleCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(30*60);
        return simpleCookie;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(simpleCookie());
        return cookieRememberMeManager;
    }
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        Map<String,String> filterChainDefinition = new LinkedHashMap<>();
        /*filterChainDefinition.put("/admin/logout","logout");*/
        filterChainDefinition.put("/admin/css/**","anon");
        filterChainDefinition.put("/admin/fonts/**","anon");
        filterChainDefinition.put("/admin/img/**","anon");
        filterChainDefinition.put("/admin/js/**","anon");
        filterChainDefinition.put("/admin/plugins/**","anon");
        filterChainDefinition.put("/admin/login","anon");
        filterChainDefinition.put("/admin/**","authc");
        filterChainDefinition.put("/swagger-ui.html","authc");
        filterChainDefinition.put("/v2/**","authc");
        filterChainDefinition.put("/swagger-resources/**","authc");
        filterFactoryBean.setLoginUrl("/admin/login");
        filterFactoryBean.setSuccessUrl("/admin/index");
        filterFactoryBean.setFilterChainDefinitionMap(filterChainDefinition);
        return filterFactoryBean;
    }
    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager){
        MethodInvokingFactoryBean invokingFactoryBean = new MethodInvokingFactoryBean();
        invokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        invokingFactoryBean.setArguments(new Object[]{securityManager});
        return invokingFactoryBean;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

}
