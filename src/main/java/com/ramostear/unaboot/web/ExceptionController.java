package com.ramostear.unaboot.web;

import com.ramostear.unaboot.exception.NotFoundException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/7/1 0001 19:25.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
public class ExceptionController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping("/error")
    public void  error(){
        throw new NotFoundException("request error!");
    }
}
