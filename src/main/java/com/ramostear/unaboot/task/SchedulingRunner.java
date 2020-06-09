package com.ramostear.unaboot.task;

import com.ramostear.unaboot.component.SpringContextAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 14:43.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Slf4j
public class SchedulingRunner implements Runnable {

    private String bean;
    private String method;
    private String cronExp;
    private String params;


    public SchedulingRunner(String bean,String method,String cronExp,String params){
        this.bean = bean;
        this.method = method;
        this.cronExp = cronExp;
        this.params = params;
    }

    public SchedulingRunner(String bean,String method,String cronExp){
        this(bean,method,cronExp,"");
    }


    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            Object target = SpringContextAware.getBean(bean);
            Method _method;
            if(StringUtils.isNotEmpty(params)){
                _method = target.getClass().getDeclaredMethod(method,String.class);
            }else{
                _method = target.getClass().getDeclaredMethod(method);
            }
            ReflectionUtils.makeAccessible(_method);
            if(StringUtils.isNotEmpty(params)){
                _method.invoke(target,params);
            }else{
                _method.invoke(target);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Scheduled task execution failed : {}",e.getMessage());
        }
        long end = System.currentTimeMillis();

        log.info("Scheduled task completed - bean:{},method:{},params:{},times:{}",bean,method,params,end-start);
    }

    @Override
    public int hashCode(){
        if(params == null){
            return Objects.hash(bean,method,cronExp);
        }else{
            return Objects.hash(bean,method,cronExp,params);
        }
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        SchedulingRunner t = (SchedulingRunner)o;
        if(params == null){
            return bean.equals(t.bean)
                    && method.equals(t.method)
                    && cronExp.equals(t.cronExp)
                    && t.params == null;
        }
        return bean.equals(t.bean)
                && method.equals(t.method)
                && cronExp.equals(t.cronExp)
                && params.equals(t.params);

    }

    @Override
    public String toString() {
        return "SchedulingRunner{" +
                "bean='" + bean + '\'' +
                ", method='" + method + '\'' +
                ", cronExp='" + cronExp + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
