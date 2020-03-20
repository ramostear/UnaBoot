package com.ramostear.unaboot.web;

import com.ramostear.unaboot.common.exception.NotFoundException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName ErrorExceptionController
 * @Description 自定义前端全局的404/500页面
 * @Author 树下魅狐
 * @Date 2020/3/19 0019 6:48
 * @Version since UnaBoot-1.0
 **/
@Controller
public class ErrorExceptionController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping("/error")
    public void error(){
        throw new NotFoundException("Global blog exception.");
    }

}
