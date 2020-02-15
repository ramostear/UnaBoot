package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.service.SettingService;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.web.UnaBootController;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;

/**
 * @ClassName SettingController
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/15 0015 15:17
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Controller
@RequestMapping("/admin/setting")
public class SettingController extends UnaBootController {

    private final SettingService settingService;
    private final ThemeService themeService;
    private final ServletContext servletContext;

    public SettingController(SettingService settingService, ServletContext servletContext,ThemeService themeService){
        this.settingService = settingService;
        this.themeService = themeService;
        this.servletContext = servletContext;
    }

    @GetMapping("/general")
    public String general(Model model){
        model.addAttribute("general",settingService.convertTo());
        model.addAttribute("themes",themeService.loadThemes());
        return "/admin/setting/general";
    }

    @PostMapping("/general")
    @ResponseBody
    public ResponseEntity<Object> general(HttpServletRequest request){
        return updateSetting(request);
    }


    private ResponseEntity<Object> updateSetting(HttpServletRequest request){
        Map<String,String[]> parameters = request.getParameterMap();
        List<Setting> items = new ArrayList<>();
        parameters.forEach((key,value)->{
            items.add(Setting.builder().key(key).value(value[0]).build());
        });

        settingService.update(items);
        Map<String,Setting> settings = settingService.convertTo();
        Set<String> keySet = settings.keySet();
        keySet.forEach(key->{
            servletContext.setAttribute(key,settings.get(key).getValue());
            log.info("General setting key:[{}],value:[{}]",key,settings.get(key).getValue());
        });
        return ok();
    }

}
