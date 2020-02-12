package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.domain.valueobject.ThemeFile;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
    public List<ThemeFile> themeFiles(@RequestParam(name = "id",defaultValue = "") String folder){
        List<ThemeFile> files = themeService.loadThemeFile(folder,false);
        return files;
    }

    @GetMapping("/subFiles")
    public String subFiles(@RequestParam(name = "pid",defaultValue = "")String folder, Model model){
        List<ThemeFile> files = themeService.loadThemeFile(folder,true);
        model.addAttribute("files",files);
        return "/admin/theme/subFile";
    }

    /**
     * Go to upload theme file page.
     * @return
     */
    @GetMapping("/upload")
    public String upload(){
        return "/admin/theme/upload";
    }

    /**
     * Response upload theme file request
     * @param file          theme file(.zip/.rar)
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity<Object> upload(@RequestParam("theme")CommonsMultipartFile file, HttpServletRequest request){
        Theme theme = themeService.install(request,file);
        if(theme != null){
            clear(file.getOriginalFilename());
            return ok();
        }else{
            return badRequest();
        }
    }

    @GetMapping("/editor")
    public String editor(@RequestParam("path")String path,Model model){
        model.addAttribute("path",path);
        return "/admin/theme/editor";
    }

    @ResponseBody
    @GetMapping("/read")
    public String readContent(@RequestParam("path")String path){
        return themeService.loadThemeFileContent(path);
    }

    private static void clear(String fileName){
        System.gc();
        File delFile = new File(UnaBootConst.FILE_UPLOAD_ROOT_DIR+"themes"+UnaBootConst.SEPARATOR+fileName);
        delFile.delete();
    }
}
