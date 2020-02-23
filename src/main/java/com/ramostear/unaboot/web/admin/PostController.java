package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.common.util.RandomUtils;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.valueobject.PostQuery;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.web.UnaBootController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;

/**
 * @ClassName PostController
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/23 0023 8:32
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Controller
@RequestMapping("/admin/post")
public class PostController extends UnaBootController {

    @Autowired
    private PostService postService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ServletContext servletContext;


    @GetMapping("/index")
    public String index(PostQuery query, Model model){
        Page<Post> posts = postService.pageBy(query,pageByDesc("updateTime"));
        model.addAttribute("data",postService.convert(posts))
             .addAttribute("query",query)
             .addAttribute("urlParam",urlParam(query))
             .addAttribute("categories",categoryService.findAll());
        return "/admin/post/index";
    }

    @GetMapping("/write")
    public String write(Model model){
        String theme = (String) servletContext.getAttribute("theme");
        if(StringUtils.isEmpty(theme)){
            theme = "default";
        }
        model.addAttribute("slug",initSlug())
             .addAttribute("themes",themeService.templateDetail(theme))
             .addAttribute("categories",categoryService.findAll());
        return "/admin/post/write";
    }

    private String urlParam(PostQuery query){
        if(query == null){
            return "";
        }
        String url = "";
        if(query.getStatus() != null && (query.getStatus() == 0 || query.getStatus() == 1)){
            url+="&status="+query.getStatus();
        }
        if(StringUtils.isNotBlank(query.getKey())){
            url+= "&key="+query.getKey();
        }
        if(query.getCategory() != null && query.getCategory() > 0){
            url+= "&category="+query.getCategory();
        }
        if(url.trim().length() <=1){
            return "";
        }else{
            return "?"+url.substring(1);
        }
    }

    private String initSlug(){
        return new SimpleDateFormat("yyyy/MM/dd")
                .format(DateTimeUtils.current())+"/"+ RandomUtils.string(8)+".html";
    }
}
