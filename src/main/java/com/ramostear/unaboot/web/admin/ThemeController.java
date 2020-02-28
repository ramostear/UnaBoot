package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.domain.valueobject.ThemeFile;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@RequiresRoles(value = UnaBootConst.ROLE_ADMIN)
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

    /**
     * Jump to the editor page
     * @param path
     * @param model
     * @return
     */
    @GetMapping("/editor")
    public String editor(@RequestParam("path")String path,Model model){
        model.addAttribute("path",path);
        return "/admin/theme/editor";
    }

    /**
     * Read file content
     * @param path
     * @return
     */
    @ResponseBody
    @GetMapping("/read")
    public String readContent(@RequestParam("path")String path){
        return themeService.loadThemeFileContent(path);
    }

    /**
     * Write data to template file
     * @param path
     * @param content
     * @return
     */
    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity<Object> writeContent(@RequestParam("path")String path,@RequestParam("content") String content){
        boolean flag = themeService.writeContentToThemeFile(path, content);
        if(flag){
            return ok();
        }else{
            return badRequest();
        }
    }
    @ResponseBody
    @PostMapping("/newFile")
    public ResponseEntity<Object> newFile(@RequestParam("path")String path){
        boolean flag = themeService.newFile(path);
        if(flag){
            return ok();
        }else{
            return badRequest();
        }
    }

    @ResponseBody
    @PostMapping("/newFolder")
    public ResponseEntity<Object> newFolder(@RequestParam("path")String path){
        boolean flag = themeService.newFolder(path);
        if(flag){
            return ok();
        }else{
            return badRequest();
        }
    }

    @ResponseBody
    @PostMapping("/deleteFile")
    public ResponseEntity<Object> deleteFile(@RequestParam("path")String path){
        //TODO 如果删除整个主题文件，则需要判断该主题是否正在被使用，如果当前主题已经被使用，则不允许删除整个主题文件
        boolean flag = themeService.deleteFile(path);
        if(flag){
            return ok();
        }else{
            return badRequest();
        }
    }


    private static void clear(String fileName){
        System.gc();
        File delFile = new File(UnaBootConst.FILE_UPLOAD_ROOT_DIR+"themes"+UnaBootConst.SEPARATOR+fileName);
        delFile.delete();
    }
}
