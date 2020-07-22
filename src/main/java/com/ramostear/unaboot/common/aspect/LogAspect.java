package com.ramostear.unaboot.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.ramostear.unaboot.common.aspect.lang.UnaLog;
import com.ramostear.unaboot.domain.entity.OperLog;
import com.ramostear.unaboot.event.LogEvent;
import com.ramostear.unaboot.util.IpUtils;
import com.ramostear.unaboot.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Joinpoint;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/7/22 0022 12:11.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    private final ApplicationEventPublisher applicationEventPublisher;

    LogAspect(ApplicationEventPublisher applicationEventPublisher){
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Pointcut("@annotation(com.ramostear.unaboot.common.aspect.lang.UnaLog)")
    public void logPointCut(){}


    @AfterReturning(pointcut = "logPointCut()")
    public void afterReturning(JoinPoint point){
        handleLog(point,null);
    }



    @AfterThrowing(value = "logPointCut()",throwing = "e")
    public void afterThrowing(JoinPoint point,Exception e){
        handleLog(point,e);
    }

    private void handleLog(final JoinPoint point, final Exception e) {
        UnaLog log = getAnnotation(point);
        if(log == null){
            return;
        }
        OperLog operLog = new OperLog();
        operLog.setUrl(ServletUtils.getRequest().getRequestURI());
        operLog.setIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        if(e != null){
            operLog.setError(StringUtils.substring(e.getMessage(),0,1000));
        }
        String className = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        operLog.setMethod(className+ "."+ methodName + "()");
        setLogInfo(log,operLog);
        applicationEventPublisher.publishEvent(new LogEvent(this,operLog));
    }

    public void setLogInfo(UnaLog log,OperLog operLog){
        operLog.setType(log.type().getText());
        operLog.setTitle(log.title());
        setRequestParameters(operLog);
    }

    private void setRequestParameters(OperLog log){
        Map<String,String[]> map = ServletUtils.getRequest().getParameterMap();
        String params = JSONObject.toJSONString(map);
        log.setParams(StringUtils.substring(params,0,2000));
    }


    private UnaLog getAnnotation(JoinPoint point) {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method method = methodSignature.getMethod();
        if(method != null){
            return method.getAnnotation(UnaLog.class);
        }
        return null;
    }
}
