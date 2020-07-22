package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.aspect.lang.LogType;
import com.ramostear.unaboot.common.aspect.lang.UnaLog;
import com.ramostear.unaboot.component.FileManager;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.vo.CategoryVo;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.service.PostCategoryService;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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

    private final PostCategoryService postCategoryService;

    private final ServletContext servletContext;

    private final ThemeService themeService;

    private final FileManager fileManager;

    private final PostService postService;

    @Autowired
    CategoryController(CategoryService categoryService,ServletContext servletContext,
                       ThemeService themeService,FileManager fileManager,
                       PostService postService,PostCategoryService postCategoryService){
        this.postCategoryService = postCategoryService;
        this.categoryService = categoryService;
        this.servletContext = servletContext;
        this.themeService = themeService;
        this.fileManager = fileManager;
        this.postService = postService;
    }

    @UnaLog(title = "栏目列表",type = LogType.LIST)
    @GetMapping("/")
    public String categories(){
        return "/admin/category/list";
    }

    @UnaLog(title = "栏目树",type = LogType.VIEW)
    @GetMapping(value = "/",params = {"type=tree"})
    @ResponseBody
    public CategoryVo nodes(){
        return categoryService.tree(Sort.by(Sort.Direction.ASC,"sortId"));
    }

    @UnaLog(title = "获取子栏目",type = LogType.LIST)
    @GetMapping("/{pid:\\d+}/children")
    public String children(@PathVariable("pid")Integer pid,Model model){
        List<Category> list = categoryService.findAllByPid(pid);
        model.addAttribute("data",list);
        return "/admin/category/children";
    }

    @UnaLog(title = "新增栏目",type = LogType.VIEW)
    @GetMapping("/{pid:\\d+}/create")
    public String create(@PathVariable("pid")Integer pid, Model model){
        String theme =currentTheme();
        Category parent = categoryService.findById(pid);
        List<String> tpls = themeService.filter(theme,".html");
        model.addAttribute("parent",parent)
             .addAttribute("tpls",tpls);
        return "/admin/category/create";
    }

    @UnaLog(title = "新增栏目",type = LogType.INSERT)
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

    @UnaLog(title = "修改栏目",type = LogType.VIEW)
    @GetMapping("/{id:\\d+}/update")
    public String category(@PathVariable("id")Integer id,Model model){
       String theme = currentTheme();
       List<String> tpls = themeService.filter(theme,".html");
       Category category = categoryService.findById(id);
       model.addAttribute("category",category)
            .addAttribute("tpls",tpls);
       return "/admin/category/view";
    }

    @UnaLog(title = "修改栏目",type = LogType.UPDATE)
    @ResponseBody
    @PostMapping("/{id:\\d+}/update")
    public ResponseEntity<Object> category(@PathVariable("id")Integer id,Category category){
        Category original = categoryService.findByIdNullable(id);
        if(original == null){
            return bad();
        }else{
            String thumb = original.getThumb();
            String postTheme = original.getPostTheme();
            BeanUtils.copyProperties(category,original,"id","createTime","updateTime");
            categoryService.update(original);
            if(StringUtils.isNotBlank(thumb)){
                fileManager.remove(thumb);
            }
            if(!postTheme.equals(original.getPostTheme())){
                postTheme = original.getPostTheme();
                List<Post> postList = postCategoryService.findAllPostByCategoryId(original.getId());
                if(!CollectionUtils.isEmpty(postList)){
                    for(Post post : postList){
                        post.setTpl(postTheme);
                    }
                    postService.update(postList);
                }
            }
            return ok();
        }
    }

    @UnaLog(title = "删除栏目",type = LogType.DELETE)
    @ResponseBody
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        try {
            categoryService.delete(id);
            return ok();
        }catch (UnaBootException e){
            return bad(e.getMessage());
        }
    }

    @UnaLog(title = "上传缩略图",type = LogType.UPLOAD)
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
