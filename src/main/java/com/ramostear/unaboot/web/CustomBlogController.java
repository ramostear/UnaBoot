package com.ramostear.unaboot.web;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.exception.BlogNotFoundException;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.PostCategory;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.service.PostCategoryService;
import com.ramostear.unaboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * @ClassName CustomBlogController
 * @Description 自定义的博客控制器（BlogController是前端博客通用的控制器，自定义的它控制器在这里进行定义）
 * @Author 树下魅狐
 * @Date 2020/3/18 0018 6:14
 * @Version since UnaBoot-1.0
 **/
@Controller
public class CustomBlogController extends UnaBootController {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostCategoryService postCategoryService;

    @GetMapping("/doc/{slug}")
    public String document(@PathVariable("slug")String slug, Model model){
        Category root = categoryService.getBySlug(slug);
        if(root == null){
            throw new BlogNotFoundException("不存在的文档或文档未发布");
        }
        Post document = postCategoryService.findTopPostByCategoryId(root.getId());
        if(document == null || document.getStatus() != UnaBootConst.ACTIVE){
            throw new BlogNotFoundException("不存在的文档或文档未发布");
        }
        List<Category> children = categoryService.findByParent(root.getId());
        model.addAttribute("root",root)
             .addAttribute("children",children)
             .addAttribute("current",root)
             .addAttribute("document",document);
        return blogView("/"+root.getTheme());
    }

    @GetMapping("/doc/{slug}/chapter/{name}")
    public String chapter(@PathVariable("slug")String slug,@PathVariable("name")String name, Model model){
        Category root = categoryService.getBySlug(slug);
        if(root == null){
            throw new BlogNotFoundException("不存在的文档分类");
        }
        Category category = categoryService.getBySlug(name);
        if(category == null){
            throw new BlogNotFoundException("不存在的栏目");
        }
        Post document = postCategoryService.findTopPostByCategoryId(category.getId());
        if(document == null || document.getStatus() != UnaBootConst.ACTIVE){
            throw new BlogNotFoundException("不存在的文档或文档未发布");
        }
        List<Category> children = categoryService.findByParent(root.getId());
        model.addAttribute("root",root)
                .addAttribute("children",children)
                .addAttribute("current",category)
                .addAttribute("document",document);
        return blogView("/"+category.getTheme());
    }

    @GetMapping("/doc/{slug}/chapter/{name}/{id}")
    public String document(@PathVariable("slug")String slug,@PathVariable("name")String name,@PathVariable("id")Integer id, Model model){
        Category root = categoryService.getBySlug(slug);
        if(root == null){
            throw new BlogNotFoundException("不存在的文档分类");
        }
        Category category = categoryService.getBySlug(name);
        if(category == null){
            throw new BlogNotFoundException("不存在的栏目");
        }
        Post document = postService.findById(id);
        if(document == null || document.getStatus() != UnaBootConst.ACTIVE){
            throw new BlogNotFoundException("不存在的文档或文档未发布");
        }
        List<Category> children = categoryService.findByParent(root.getId());
        model.addAttribute("root",root)
                .addAttribute("children",children)
                .addAttribute("current",category)
                .addAttribute("document",document);
        return blogView("/"+document.getTheme());
    }

    private String blogView(String path){
        String theme = (String) servletContext.getAttribute("theme");
        if(path.endsWith(".html")){
            return "/"+theme+path.substring(0,path.indexOf("."));
        }else{
            return "/"+theme+path;
        }
    }
}
