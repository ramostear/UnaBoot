package com.ramostear.unaboot.task;

import com.ramostear.unaboot.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @ClassName TaskSchedulingRunnable
 * @Description Task
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 18:14
 * @Version since UnaBoot-1.0
 **/
@Slf4j
public class TaskSchedulingRunnable implements Runnable{

    private String beanName;

    private String methodName;

    private String params;

    public TaskSchedulingRunnable(String beanName,String methodName){
        this(beanName,methodName,null);
    }

    public TaskSchedulingRunnable(String beanName,String methodName,String params){
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
    }

    @Override
    public void run() {
        log.info("UnaBootTaskScheduler on start - bean:{},method:{},params:{}",beanName,methodName,params);
        long startTime = System.currentTimeMillis();
        try {
            Object target = SpringContextUtils.getBean(beanName);
            Method method;
            if(StringUtils.isNotEmpty(params)){
                method = target.getClass().getDeclaredMethod(methodName,String.class);
            }else{
                method = target.getClass().getDeclaredMethod(methodName);
            }
            ReflectionUtils.makeAccessible(method);
            if(StringUtils.isNotEmpty(params)){
                method.invoke(target,params);
            }else{
                method.invoke(target);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Execute scheduling task error - bean:{},method:{},params:{},error message:{}",beanName,methodName,params,e.getMessage());
        }
        long times = System.currentTimeMillis() - startTime;
        log.info("Execute scheduling task finished - bean:{},method:{},params:{},times:{}",beanName,methodName,params,times);
    }

    @Override
    public int hashCode() {
        if(params == null){
            return Objects.hash(beanName,methodName);
        }
        return Objects.hash(beanName,methodName,params);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null || getClass() != obj.getClass()){
            return false;
        }
        TaskSchedulingRunnable that = (TaskSchedulingRunnable)obj;
        if(params == null){
            return beanName.equals(that.beanName)
                    && methodName.equals(that.methodName)
                    && that.params == null;
        }
        return beanName.equals(that.beanName)
                && methodName.equals(that.methodName)
                && params.equals(that.params);

    }

    @Override
    public String toString() {
        return "TaskSchedulingRunnable{" +
                "beanName='" + beanName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
