package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.SortType;
import com.ramostear.unaboot.domain.entity.OperLog;
import com.ramostear.unaboot.service.OperLogService;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/7/22 0022 13:44.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
@RequestMapping("/admin/logs")
@RequiresRoles(value = {"admin"})
public class LogController extends UnaBootController {

    private final OperLogService operLogService;

    LogController(OperLogService operLogService){
        this.operLogService = operLogService;
    }

    @GetMapping("/")
    public String list(Model model){
        Page<OperLog> data = operLogService.findAll(pageable("createTime", SortType.DESC));
        model.addAttribute("data",data);
        return "/admin/log/list";
    }

}
