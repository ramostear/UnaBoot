package com.ramostear.unaboot.interceptor;

import com.ramostear.unaboot.common.util.UnaBootUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName InstallerInterceptor
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 8:52
 * @Version since UnaBoot-1.0
 **/
@Component
@Slf4j
public class InstallerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(UnaBootUtils.isInstall()){
            return super.preHandle(request, response, handler);
        }else{
            log.info("UnaBoot System has not been install,Please install and init this application");
            response.sendRedirect("/install.html");
            return false;
        }
    }
}
