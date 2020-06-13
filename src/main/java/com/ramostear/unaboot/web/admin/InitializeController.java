package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.domain.dto.InitializeDto;
import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.service.InitializeService;
import com.ramostear.unaboot.service.SettingService;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.util.UnaBootUtils;
import com.ramostear.unaboot.web.UnaBootController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.Set;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/12 0012 23:28.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Slf4j
@Controller
@ApiIgnore
public class InitializeController extends UnaBootController {

    @Autowired
    private InitializeService initializeService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/install.html")
    public String install(){
        if(UnaBootUtils.isInstalled()){
            return sendRedirect("/admin/login");
        }
        return "/initialize";
    }

    @ResponseBody
    @PostMapping("/install.html")
    public ResponseEntity<Object> install(InitializeDto dto){
        boolean flag = false;
        try {
            initializeService.configurer(dto);
            themeService.initialize();
            reloadSettings();
            /*UnaBootUtils.marked();*/
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag?ok():bad();
    }

    private void reloadSettings(){
        Map<String, Setting> map = settingService.toMap();
        if(!CollectionUtils.isEmpty(map)){
            Set<String> keys = map.keySet();
            keys.forEach(key->{
                servletContext.setAttribute(key,map.get(key).getValue());
                log.info("Refresh application context attribute -- key:{},value:{}",key,map.get(key).getValue());
            });
        }
    }
}
