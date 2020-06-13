package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.vo.CategoryVo;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 1:50.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
@RequestMapping("/admin/categories")
public class CategoryController extends UnaBootController {

    private final CategoryService categoryService;

    private final ServletContext servletContext;

    private final ThemeService themeService;

    @Autowired
    CategoryController(CategoryService categoryService,ServletContext servletContext,
                       ThemeService themeService){
        this.categoryService = categoryService;
        this.servletContext = servletContext;
        this.themeService = themeService;
    }

    @GetMapping("/")
    public String categories(){
        return "/admin/category/list";
    }

    @GetMapping(value = "/",params = {"type=tree"})
    @ResponseBody
    public CategoryVo nodes(){
        return categoryService.tree(Sort.by(Sort.Direction.ASC,"sortId"));
    }

    @GetMapping("/{pid:\\d+}/children")
    public String children(@PathVariable("pid")Integer pid,Model model){
        List<Category> list = categoryService.findAllByPid(pid);
        model.addAttribute("data",list);
        return "/admin/category/children";
    }

    @GetMapping("/{pid:\\d+}/create")
    public String create(@PathVariable("pid")Integer pid, Model model){
        String theme =currentTheme();
        Category parent = categoryService.findById(pid);
        List<String> tpls = themeService.filter(theme,".html");
        model.addAttribute("parent",parent)
             .addAttribute("tpls",tpls);
        return "/admin/category/create";
    }

    @ResponseBody
    @PostMapping("/{pid:\\d+}/create")
    public ResponseEntity<Object> create(@PathVariable("pid")Integer pid,Category category){
        if(pid < 0){
            pid = 0;
        }
        category.setPid(pid);
        try {
            categoryService.create(category);
            return ok();
        }catch (Exception e){
            return bad();
        }
    }

    @GetMapping("/{id:\\d+}/update")
    public String category(@PathVariable("id")Integer id,Model model){
       String theme = currentTheme();
       List<String> tpls = themeService.filter(theme,".html");
       Category category = categoryService.findById(id);
       model.addAttribute("category",category)
            .addAttribute("tpls",tpls);
       return "/admin/category/view";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}/update")
    public ResponseEntity<Object> category(@PathVariable("id")Integer id,Category category){
        Category original = categoryService.findByIdNullable(id);
        if(original == null){
            return bad();
        }else{
            BeanUtils.copyProperties(category,original,"id","createTime","updateTime");
            categoryService.update(original);
            return ok();
        }
    }

    @ResponseBody
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        try {
            categoryService.delete(id);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @GetMapping("/thumb")
    public String thumb(){
        return "/admin/category/thumb";
    }

    private  String currentTheme(){
        String theme = (String) servletContext.getAttribute("theme");
        if(StringUtils.isBlank(theme)){
            theme = "default";
        }
        return theme;
    }

}
