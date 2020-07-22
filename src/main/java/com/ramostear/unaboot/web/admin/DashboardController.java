package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.aspect.lang.LogType;
import com.ramostear.unaboot.common.aspect.lang.UnaLog;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/13 0013 1:15.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
public class DashboardController extends UnaBootController {

    @UnaLog(title = "访问后台主页",type = LogType.VIEW)
    @GetMapping("/admin/index")
    public String dashboard(Model model){
        User user = currentUser();
        if(user == null){
            return sendRedirect("/admin/login");
        }
        return "/admin/index";
    }

    @UnaLog(title = "API文档",type = LogType.VIEW)
    @GetMapping("/admin/apis")
    public String apis(){
        return "/admin/apis";
    }

    @UnaLog(title = "数据源",type = LogType.VIEW)
    @GetMapping("/admin/datasource")
    public String druid(){
        return "/admin/druid";
    }

}
