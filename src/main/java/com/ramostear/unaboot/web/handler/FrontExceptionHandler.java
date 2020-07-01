package com.ramostear.unaboot.web.handler;

import com.ramostear.unaboot.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/7/1 0001 19:29.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Slf4j
@ControllerAdvice(assignableTypes = {com.ramostear.unaboot.web.ExceptionController.class})
public class FrontExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public String notFoundHandler(HttpServletRequest request,NotFoundException ex){
      int code = (Integer)request.getAttribute("javax.servlet.error.status_code");
      log.info("HttpServletRequest status code:{}",code);
      log.info(ex.getMessage());
      return "redirect:/404.html";
    }
}
