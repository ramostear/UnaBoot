package com.ramostear.unaboot.web.admin;

import com.ramostear.captcha.HappyCaptcha;
import com.ramostear.captcha.support.CaptchaStyle;
import com.ramostear.captcha.support.CaptchaType;
import com.ramostear.unaboot.domain.param.LoginParam;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/12 0012 19:40.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
@ApiIgnore
public class LoginController extends UnaBootController {

    @GetMapping("/admin/login")
    public String login(){
        if(currentUser() != null){
            return sendRedirect("/admin/index");
        }else{
            return "/login";
        }
    }

    @PostMapping("/admin/login")
    @ResponseBody
    public ResponseEntity<Object> login(@Valid @RequestBody LoginParam param,BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return bad(Objects.requireNonNull(bindingResult.getFieldError()).toString());
        }
        if(!HappyCaptcha.verification(request,param.getCaptcha(),true)){
            return bad("请检查您的验证码是否正确");
        }
        HappyCaptcha.remove(request);
       try {
           UsernamePasswordToken token = new UsernamePasswordToken(param.getAccount(),param.getPassword(),param.isRememberMe());
           Subject subject = SecurityUtils.getSubject();
           subject.login(token);
           if(subject.isAuthenticated()){
               SavedRequest savedRequest = WebUtils.getSavedRequest(request);
               if(savedRequest == null || savedRequest.getRequestUrl() == null){
                   return ok();
               }else{
                   return ok(savedRequest.getRequestUrl());
               }
           }else{
               return bad("用户名或密码不正确");
           }
       }catch (LockedAccountException e){
           return bad("账号已被锁定，请联系站点管理员");
       }catch (AuthenticationException e){
           return bad("您无权登录该站点，请联系站点管理员");
       }catch (UnaBootException e){
           return bad("登录异常，请联系站点管理员");
       }
    }

    @RequiresUser
    @GetMapping("/admin/logout")
    public String logout(HttpServletResponse response, Model model){
        SecurityUtils.getSubject().logout();
        response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        model.addAttribute("logoutMsg","你已注销");
        return sendRedirect("/admin/login");
    }

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request,HttpServletResponse response){
        HappyCaptcha.require(request, response)
                    .style(CaptchaStyle.ANIM)
                    .length(6)
                    .build().finish();
    }
}
