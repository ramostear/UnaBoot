package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.SortType;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.vo.QueryParam;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.util.DateTimeUtils;
import com.ramostear.unaboot.util.UnaBootUtils;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 16:24.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
@RequestMapping("/admin/posts")
public class PostController extends UnaBootController {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private final PostService postService;
    private final CategoryService categoryService;

    @Autowired
    public PostController(PostService postService,CategoryService categoryService){
        this.postService = postService;
        this.categoryService = categoryService;
    }


    @GetMapping("/")
    public String posts(QueryParam param, Model model){
        Page<Post> data = postService.page(param,pageable("createTime", SortType.DESC));
        model.addAttribute("data",postService.valueOf(data))
             .addAttribute("param",param)
             .addAttribute("urlParam",urlParam(param))
             .addAttribute("categories",categoryService.findAll());
        return "/admin/post/list";
    }





    private String urlParam(QueryParam param){
        if(param == null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if(param.getStatus() > Integer.MIN_VALUE && (param.getStatus() == -1 || param.getStatus() == 0 || param.getStatus() == 1)){
            sb.append("&status=").append(param.getStatus());
        }
        if(param.getStyle() > Integer.MIN_VALUE && (param.getStyle() == 0 || param.getStyle()==1)){
            sb.append("&style=").append(param.getStyle());
        }
        if(StringUtils.isNotBlank(param.getKey())){
            sb.append("&key=").append(param.getKey());
        }
        if(param.getCategory() > 0){
            sb.append("&category=").append(param.getCategory());
        }
        String urlParam = sb.toString();
        if(urlParam.trim().length() <=1){
            return "";
        }else{
            return "?"+urlParam.substring(1);
        }
    }

    private String initializeSlug(){
        return SIMPLE_DATE_FORMAT.format(DateTimeUtils.now())+"/"+ UnaBootUtils.random(8)+".html";
    }
}
