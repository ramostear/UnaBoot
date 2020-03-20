package com.ramostear.unaboot.web;

import com.ramostear.unaboot.common.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName GlobalBlogExceptionHandler
 * @Description 全局的博客异常处理类
 * @Author 树下魅狐
 * @Date 2020/3/19 0019 6:28
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@ControllerAdvice(assignableTypes = {com.ramostear.unaboot.web.ErrorExceptionController.class})
public class GlobalBlogExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public String notfoundHandler(HttpServletRequest request,NotFoundException ex){
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        log.info("Http servlet request status code:{}",statusCode);
        log.info(ex.getMessage());
        return "redirect:/404.html";
    }
}
