package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.exception.UnaBootException;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.valueobject.CategoryVo;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
 * @ClassName CategoryController
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/15 0015 1:00
 * @Version since UnaBoot-1.0
 **/
@RequiresRoles(value = UnaBootConst.ROLE_ADMIN)
@Controller
@RequestMapping("/admin/category")
public class CategoryController extends UnaBootController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ThemeService themeService;

    @GetMapping("/index")
    public String index(){
        return "/admin/category/index";
    }

    @GetMapping("/treeNodes")
    @ResponseBody
    public CategoryVo treeNodes(){
        return categoryService.treeNodes(Sort.by(Sort.Direction.ASC,"sortId"));
    }


    @GetMapping("/subData")
    public String subData(@RequestParam(name = "pid",defaultValue = "0")Integer pid, Model model){
        List<Category> categories = categoryService.findByParent(pid);
        model.addAttribute("categories",categories);
        return "/admin/category/subData";
    }

    @GetMapping("/{parent:\\d+}/create")
    public String create(@PathVariable(name="parent")Integer parent, Model model){
        String theme = (String) servletContext.getAttribute("theme");
        if(StringUtils.isEmpty(theme)){
            theme = "default";
        }
        List<String> templates = themeService.templateDetail(theme);
        model.addAttribute("parent",parent)
             .addAttribute("templates",templates);
        return "/admin/category/create";
    }

    @ResponseBody
    @PostMapping(value = "/{parent:\\d+}/create")
    public ResponseEntity<Object> create(@PathVariable("parent")Integer pid,Category category){
        if(pid <0){
            pid = 0;
            category.setPid(pid);
        }
        categoryService.create(category);
        if(category.getId()>0){
            return ok();
        }else{
            return badRequest();
        }
    }

    @GetMapping("/{id:\\d+}/update")
    public String update(@PathVariable("id")Integer id,Model model){
        String theme = (String) servletContext.getAttribute("theme");
        if(StringUtils.isEmpty(theme)){
            theme = "default";
        }
        List<String> templates = themeService.templateDetail(theme);
        Category category = categoryService.findById(id);
        model.addAttribute("category",category)
             .addAttribute("templates",templates);
        return "/admin/category/update";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}/update")
    public ResponseEntity<Object> update(@PathVariable("id")Integer id,Category category){
        Category target = categoryService.findById(id);
        if(target == null){
            return badRequest();
        }else{
            BeanUtils.copyProperties(category,target,new String[]{"id","createTime"});
            categoryService.update(target);
            return ok();
        }
    }

    @DeleteMapping("/{id:\\d+}")
    @ResponseBody
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        try {
            categoryService.deleteCategoryAndRelationById(id);
            return ok();
        }catch (UnaBootException e){
            return badRequest(e.getMessage());
        }
    }





}
