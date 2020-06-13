package com.ramostear.unaboot.web.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/12 0012 18:39.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component
public class UnaBootServletContextHandler extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.setAttribute("una",request.getContextPath());
        super.postHandle(request, response, handler, modelAndView);
    }
}
