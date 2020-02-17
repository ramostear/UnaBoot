package com.ramostear.unaboot.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @ClassName SpringContextUtils
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 18:20
 * @Version since UnaBoot-1.0
 **/
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
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
