package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.exception.UnaBootException;
import com.ramostear.unaboot.common.util.UnaBootUtils;
import com.ramostear.unaboot.domain.dto.InstallDto;
import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.domain.param.LoginParam;
import com.ramostear.unaboot.domain.valueobject.VerifyCodeVo;
import com.ramostear.unaboot.service.InstallService;
import com.ramostear.unaboot.service.SettingService;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.service.VerifyCodeGenService;
import com.ramostear.unaboot.web.UnaBootController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AdminController
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 9:00
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Controller
public class AdminController extends UnaBootController {

    @Autowired
    private InstallService installService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private VerifyCodeGenService verifyCodeGenService;
    private static final String DEFAULT_MSG = "用户名或密码错误";

    @GetMapping("/unaboot/install.html")
    public String install(){
        if(UnaBootUtils.isInstall()){
            return redirect("/admin/index");
        }
        return "/admin/install";
    }

    @ResponseBody
    @PostMapping("/unaboot/install.html")
    public ResponseEntity<Object> install(InstallDto dto){
        boolean flag = false;
        try {
            installService.install(dto);
            themeService.initDefaultTheme();
            refreshContext();
            UnaBootUtils.writeInstallFile();
            flag = true;
        } catch (IOException | InstantiationException | SQLException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(flag){
            return ok();
        }else{
            return badRequest();
        }
    }

    @RequiresRoles(value = UnaBootConst.ROLE_ADMIN)
    @GetMapping("/admin/index")
    public String index(){
        return "/admin/index";
    }

    @GetMapping("/admin/login")
    public String login(){
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("profile");
        if(user != null){
            return redirect("/admin/index");
        }else{
            return "/admin/login";
        }
    }

    @PostMapping("/admin/login")
    @ResponseBody
    public ResponseEntity<Object> login(@Valid @RequestBody LoginParam param, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return badRequest(DEFAULT_MSG);
        }
        String verifyCode = (String) request.getSession().getAttribute("verifyCode");
        if(StringUtils.isBlank(verifyCode) || !verifyCode.equals(param.getVerifyCode())){
            log.info("user login verify code error");
            return badRequest("验证码不正确");
        }
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(param.getUsername(),param.getPassword());
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            if(subject.isAuthenticated()){
                log.info("user login success,account:{}",param.getUsername());
                return ok();
            }else{
                log.warn("username or password error,account:{}",param.getUsername());
                return badRequest(DEFAULT_MSG);
            }
        }catch (AccountException e){
            log.warn("username or password error:{}",e.getMessage());
            return badRequest(DEFAULT_MSG);
        }catch (AuthenticationException e){
            log.warn("login account authentication error:{}",e.getMessage());
            return badRequest(param.getUsername()+"账户没有权限登录后台");
        }catch (UnaBootException e){
            log.warn("admin login error:{}",e.getMessage());
            return badRequest("登录异常");
        }
    }

    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response){
        try{
            VerifyCodeVo  codeVo = verifyCodeGenService.generate(80,28);
            String code = codeVo.getCode();
            log.info("Login verify code :{}",code);
            request.getSession().setAttribute("verifyCode",code);
            response.setHeader("Pragma","no-cache");
            response.setHeader("Cache-Control","no-cache");
            response.setDateHeader("Expires",0);
            response.setContentType("image/jpeg");
            OutputStream os = response.getOutputStream();
            os.write(codeVo.getImgBytes());
            os.flush();
        }catch (IOException e){
            log.error("write verify code to page error :{}",e.getMessage());
        }
    }

    private void refreshContext(){
        if(UnaBootUtils.isInstall()){
            Map<String, Setting> settings = settingService.convertTo();
            Set<String> keys = settings.keySet();
            keys.forEach((String key)->{
                log.info("Refresh Application ServletContext info -- key:{},value:{}",key,settings.get(key).getValue());
                servletContext.setAttribute(key,settings.get(key).getValue());
            });
        }
    }
}
