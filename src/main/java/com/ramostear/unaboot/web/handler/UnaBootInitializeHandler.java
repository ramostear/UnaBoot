package com.ramostear.unaboot.web.handler;

import com.ramostear.unaboot.util.UnaBootUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/12 0012 18:41.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component
@Slf4j
public class UnaBootInitializeHandler extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(UnaBootUtils.isInstalled()){
            return super.preHandle(request,response,handler);
        }else{
           log.info("UnaBoot system has not been initialized,Please perform the installation steps before using the UnaBoot system.");
            response.sendRedirect("/install.html");
            return false;
        }
    }
}
