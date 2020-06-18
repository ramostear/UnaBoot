package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.domain.vo.ThemeFolder;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 12:31.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
@RequestMapping("/admin/themes")
public class ThemeController extends UnaBootController {

    private final ThemeService themeService;
    @Autowired
    ThemeController(ThemeService themeService){
        this.themeService = themeService;
    }

    @GetMapping("/")
    public String themes(@RequestParam(name = "pid",defaultValue = "themes")String pid, Model model){
        List<ThemeFolder> list = themeService.findAllByParent(pid);
        pid = pid.replace("\\","/");
        model.addAttribute("data",list)
             .addAttribute("parentId",pid);
        return "/admin/theme/list";
    }

    @GetMapping("/upload")
    public String upload(){
        return "/admin/theme/upload";
    }

    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity<Object> upload(@RequestParam("theme")CommonsMultipartFile multipartFile, HttpServletRequest request){
        try {
            Theme theme = themeService.upload(request,multipartFile);
            if(theme != null){
                clear(multipartFile.getOriginalFilename());
                return ok();
            }else{
                return bad();
            }
        }catch (Exception e){
            e.printStackTrace();
            return bad();
        }
    }

    @GetMapping("/editor")
    public String editor(@RequestParam(value = "path")String path, Model model){
        model.addAttribute("path",path);
        model.addAttribute("file",themeService.loadByUrl(path));
        return "/admin/theme/editor";
    }

    @ResponseBody
    @GetMapping("/read")
    public String read(@RequestParam("path")String path){
        return themeService.read(path);
    }

    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity<Object> write(@RequestParam("path")String path,@RequestParam("content") String content){
        return themeService.write(path,content)?ok():bad();
    }

    @ResponseBody
    @PostMapping("/newFile")
    public ResponseEntity<Object> newFile(@RequestParam("path")String path){
        return themeService.newFile(path)?ok():bad();
    }

    @ResponseBody
    @PostMapping("/newFolder")
    public ResponseEntity<Object> newFolder(@RequestParam("path")String path){
        return themeService.newFolder(path)?ok():bad();
    }

    @ResponseBody
    @PostMapping("/rename")
    public ResponseEntity<Object> rename(@RequestParam("path")String path,
                                         @RequestParam("newName")String newName){
        try {
            themeService.rename(path,newName);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @ResponseBody
    @PostMapping("/remove")
    public ResponseEntity<Object> remove(@RequestParam("paths")String paths){
        try {
            themeService.remove(paths);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @GetMapping("/download")
    public void download(@RequestParam("paths")String paths, HttpServletResponse response){
        try {
            String path = themeService.download(paths,response);
            if(path.endsWith(".zip")){
                File file = new File(path);
                if(file.exists()){
                    file.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clear(String fileName){
        System.gc();
        File destFile = new File(Constants.UNABOOT_STORAGE_DIR+"themes"+Constants.SEPARATOR+fileName);
        destFile.delete();
    }

}
