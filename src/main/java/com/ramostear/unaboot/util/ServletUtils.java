package com.ramostear.unaboot.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/7/22 0022 12:36.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class ServletUtils {

    public static String getParameter(String name){
        return getRequest().getParameter(name);
    }

    public static String getParameter(String name,String defaultValue){
        return ConvertUtils.toStr(getRequest().getParameter(name),defaultValue);
    }

    public static Integer intValue(String name){
        return ConvertUtils.toInt(getRequest().getParameter(name));
    }

    public static Integer intValue(String name,Integer defaultValue){
        return ConvertUtils.toInt(getRequest().getParameter(name),defaultValue);
    }


    public static HttpServletRequest getRequest(){
        return getRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse(){
        return getRequestAttributes().getResponse();
    }


    public static HttpSession getSession(){
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUserAgent() {
        return getRequestAttributes().getRequest().getHeader("User-Agent");
    }
}
