package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.domain.valueobject.ThemeFile;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/theme")
public class ThemeController extends UnaBootController {

    @Autowired
    private ThemeService themeService;

    @GetMapping("/index")
    public String index(){
        return "/admin/theme/index";
    }

    /**
     * Get sub files and folders by folder name
     * @param folder    target folder name
     * @return
     */
    @ResponseBody
    @GetMapping("/treeNodes")
    public List<ThemeFile> themeFiles(@RequestParam(name = "folder",defaultValue = "") String folder){
        List<ThemeFile> files = themeService.loadThemeFile(folder);
        return files;
    }
}
