package com.ramostear.unaboot.web.handler;

import com.ramostear.unaboot.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/29 0029 20:17.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Slf4j
@ControllerAdvice(basePackages = {"com.ramostear.unaboot.web.admin"})
public class AdminExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> badRequestExceptionHandler(BadRequestException e){
        log.info(e.getMessage());
        return ResponseEntity.badRequest().build();
    }

}
