package com.ramostear.unaboot.web.handler;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.UserService;
import com.ramostear.unaboot.util.AssertUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/12 0012 21:38.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component
public class RememberMeHandler extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated() && subject.isRemembered()){
            try {
                String principal = subject.getPrincipals().toString();
                Optional<User> optional = userService.findByPrincipal(principal);
                Assert.isTrue(optional.isPresent(),"账户不存在");
                User user = optional.get();
                UsernamePasswordToken token;
                if(AssertUtils.isEmail(principal)){
                    token = new UsernamePasswordToken(user.getEmail(),user.getPassword(),subject.isRemembered());
                }else{
                    token = new UsernamePasswordToken(user.getUsername(),user.getPassword(),subject.isRemembered());
                }
                subject.login(token);
                Session session = subject.getSession();
                session.setAttribute(Constants.LOGIN_SESSION_KEY,user);
                session.setTimeout(604800);
            }catch (UnaBootException e){
                response.sendRedirect("/admin/login");
                return false;
            }
            if(!subject.isAuthenticated()){
                response.sendRedirect("/admin/login");
                return false;
            }
        }
        return true;
    }
}
