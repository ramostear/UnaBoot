package com.ramostear.unaboot.web;

import com.ramostear.unaboot.common.exception.BlogNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ClassName BlogExceptionHandler
 * @Description 博客异常处理类
 * @Author 树下魅狐
 * @Date 2020/3/18 0018 6:53
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@ControllerAdvice
public class BlogExceptionHandler {
    @ExceptionHandler(value = BlogNotFoundException.class)
    public String notfoundHandler(BlogNotFoundException ex){
      log.info(ex.getMessage());
      return "redirect:/404.html";
    }
}
