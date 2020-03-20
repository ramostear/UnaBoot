package com.ramostear.unaboot.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ClassName GlobalAdminExceptionHandler
 * @Description 后台全局的异常处理类
 * @Author 树下魅狐
 * @Date 2020/3/19 0019 6:35
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@ControllerAdvice(basePackages = {"com.ramostear.unaboot.web.admin"})
public class GlobalAdminExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(Exception ex){
        log.info(ex.getMessage());
        return ResponseEntity.badRequest().build();
    }
}
