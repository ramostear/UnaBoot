package com.ramostear.unaboot.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 14:52.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component("springContextAware")
public class SpringContextAware implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextAware.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clz){
        return applicationContext.getBean(clz);
    }

    public static <T> T getBean(String beanName,Class<T> clz){
        return applicationContext.getBean(beanName,clz);
    }

    public static boolean containsBean(String beanName){
        return applicationContext.containsBean(beanName);
    }

    public static boolean isSingleton(String beanName){
        return applicationContext.isSingleton(beanName);
    }

    public static Class<? extends Object> getType(String beanName){
        return applicationContext.getType(beanName);
    }
}
