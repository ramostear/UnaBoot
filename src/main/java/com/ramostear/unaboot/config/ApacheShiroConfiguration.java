package com.ramostear.unaboot.config;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.config.support.CustomFreemarkerConfigurer;
import com.ramostear.unaboot.config.support.UnaBootRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
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
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/29 0029 15:06.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Configuration
public class ApacheShiroConfiguration {

    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache-shiro.xml");
        return ehCacheManager;
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setShared(true);
        return factoryBean;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName(Constants.ALGORITHM_NAME);
        matcher.setHashIterations(Constants.HASH_ITERATIONS);
        return matcher;
    }

    @Bean
    public UnaBootRealm unaBootRealm(){
        UnaBootRealm realm = new UnaBootRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        realm.setCachingEnabled(true);
        realm.setCacheManager(ehCacheManager());
        return realm;
    }

    @Bean
    public SimpleCookie simpleCookie(){
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setMaxAge(30*60);
        return cookie;
    }
    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(simpleCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVCXsxUs0FSA7SYFjdQg=="));
        return cookieRememberMeManager;
    }
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setCacheManager(ehCacheManager());
        securityManager.setRememberMeManager(cookieRememberMeManager());
        securityManager.setRealm(unaBootRealm());
        return securityManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor sourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        sourceAdvisor.setSecurityManager(securityManager);
        return sourceAdvisor;
    }

    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty("UnauthorizedException","/admin/login");
        exceptionResolver.setExceptionMappings(properties);
        return exceptionResolver;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        Map<String,String> filterChain = new LinkedHashMap<>();
        filterChain.put("/ub-admin/**","anon");
        filterChain.put("/admin/login","anon");
        filterChain.put("/admin/**","authc");
        filterChain.put("/swagger-ui.html","authc");
        filterChain.put("/v2/**","authc");
        filterChain.put("/swagger-resources/**","authc");
        filterChain.put("/h2-console/**","authc");
        shiroFilter.setLoginUrl("/admin/login");
        shiroFilter.setSuccessUrl("/admin/index");
        shiroFilter.setFilterChainDefinitionMap(filterChain);
        return shiroFilter;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager){
        MethodInvokingFactoryBean invokingFactoryBean = new MethodInvokingFactoryBean();
        invokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        invokingFactoryBean.setArguments(securityManager);
        return invokingFactoryBean;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
