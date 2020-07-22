package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.QiniuZone;
import com.ramostear.unaboot.common.aspect.lang.LogType;
import com.ramostear.unaboot.common.aspect.lang.UnaLog;
import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.domain.vo.QiniuProperty;
import com.ramostear.unaboot.service.SettingService;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.util.QiniuUtils;
import com.ramostear.unaboot.web.UnaBootController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 13:52.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Slf4j
@Controller
@RequestMapping("/admin/settings")
public class SettingController extends UnaBootController {

    private final SettingService settingService;
    private final ThemeService themeService;
    private final ServletContext servletContext;

    @Autowired
    SettingController(SettingService settingService,ThemeService themeService,
                      ServletContext servletContext){
        this.settingService = settingService;
        this.themeService = themeService;
        this.servletContext = servletContext;
    }

    @UnaLog(title = "获取常规配置信息",type = LogType.LIST)
    @GetMapping("/general")
    public String general(Model model){
        model.addAttribute("general",settingService.toMap());
        model.addAttribute("themes",themeService.themes());
        return "/admin/setting/general";
    }

    @UnaLog(title = "评论插件信息",type = LogType.VIEW)
    @GetMapping("/gitalk")
    public String gitalk(Model model){
        model.addAttribute("gitalk",settingService.gitalk());
        return "/admin/setting/gitalk";
    }

    @UnaLog(title = "更新系统配置",type = LogType.UPDATE)
    @ResponseBody
    @PostMapping("/update")
    public ResponseEntity<Object> update(HttpServletRequest request){
        Map<String,String[]> parameterMap = request.getParameterMap();
        List<Setting> settings = new ArrayList<>();
        parameterMap.forEach((key,value)-> {
            Setting setting = new Setting(key,value[0]);
            settings.add(setting);
        });
        settingService.updateInBatch(settings);
        Map<String,Setting> map = settingService.toMap();
        map.forEach((key,setting)->{
           servletContext.setAttribute(key,setting.getValue());
           log.info("General setting key : {} ,value : {}",key,setting.getValue());
        });
        return ok();
    }

    @UnaLog(title = "七牛云配置信息",type = LogType.VIEW)
    @GetMapping("/qiniu")
    public String qiniu(Model model){
        model.addAttribute("qiniu", QiniuUtils.getProperties());
        model.addAttribute("zones", QiniuZone.values());
        return "/admin/setting/qiniu";
    }

    @UnaLog(title = "更新七牛云配置",type = LogType.UPDATE)
    @PostMapping("/qiniu")
    @ResponseBody
    public ResponseEntity<Object> qiniu(QiniuProperty qiniu){
        boolean res = QiniuUtils.setProperties(qiniu);
        return res?ok():bad();
    }
}
