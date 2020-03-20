package com.ramostear.unaboot.web;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.exception.BlogNotFoundException;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.domain.valueobject.PostVo;
import com.ramostear.unaboot.service.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName FrontController
 * @Description 博客控制器
 * @Author 树下魅狐
 * @Date 2020/3/18 0018 4:57
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Controller
public class BlogController extends UnaBootController {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private PostService postService;
    @Autowired
    private LuceneService luceneService;
    private static CacheManager cacheManager = CacheManager.newInstance();

    /**
     * 访问博客主页
     * @return
     */
    @GetMapping(value = {"/","/index","/index.html","/home","/home.html"})
    public String index(){
        return blogView("/index.html");
    }

    /**
     * 栏目详情页面
     * @param slug      栏目自定义访问路径
     * @param offset    数据分页起始位置
     * @param model     数据模型
     * @return          theme file path
     */
    @GetMapping("/category/{slug}")
    public String category(@PathVariable("slug")String slug, @RequestParam(name = "offset",defaultValue = "1")Integer offset, Model model){
        Category category = categoryService.getBySlug(slug);
        if(category!= null){
            model.addAttribute("category",category);
            model.addAttribute("slug",slug);
            model.addAttribute("offset",offset);
            return blogView("/"+category.getTheme());
        }else{
            log.error("category is not exist:{}",slug);
            throw new BlogNotFoundException("不存在的文章分类 slug="+slug);
        }
    }

    /**
     * 访问标签列表页面，暂时不考虑分页，主题模板中需要又tags.html页面
     * @return      tags.html
     */
    @GetMapping("/tags")
    public String tags(){
        return blogView("/tags.html");
    }

    /**
     * 具体的标签页面,模板文件必须包含tag.html页面
     * @param name      标签名称
     * @param offset    数据分页起始位置
     * @param model     data Model
     * @return          tag.html
     */
    @GetMapping("/tag/{name}")
    public String tag(@PathVariable("name")String name,@RequestParam(name = "offset",defaultValue = "15")Integer offset, Model model){
        Tag tag = tagService.findByName(name);
        if(tag != null){
            model.addAttribute("tag",tag)
                 .addAttribute("offset",offset);
            return blogView("/tag.html");
        }else{
            throw new BlogNotFoundException("访问了不存在的标签");
        }
    }

    /**
     * 归档列表页面(列举所有的归档信息)，暂不考虑分页,主题文件中需要包含archives.html文件
     * @return      archives.html
     */
    @GetMapping("/archives")
    public String archives(){
        return blogView("/archives.html");
    }

    /**
     * 归档详情页面，无分页(考虑到个人博客在一个月内的文章数量不是太多，每日已更新最多才31篇文章，所以不对数据分页)
     * @param name         归档名称（2020年1月）
     * @param model         数据模型
     * @return              archive.html (模板文件中需要提供archive.html文件)
     */
    @GetMapping("/archive/{name}")
    public String archive(@PathVariable("name")String name,Model model){
       if(StringUtils.isBlank(name)){
           throw new BlogNotFoundException("不存在的文章归档");
       }else{
           model.addAttribute("archive",name);
           return blogView("/archive.html");
       }
    }

    /**
     * 全文检索
     * @param key           检索关键词
     * @param offset        数据分页起始位置
     * @param model         数据模型
     * @return              search.html(模板文件中需要提供search.html文件)
     */
    @GetMapping("/search")
    public String search(@RequestParam(name = "key",defaultValue = "",required = false)String key,@RequestParam(name = "offset",defaultValue = "1")Integer offset,Model model){
        model.addAttribute("offset",offset);
        if(StringUtils.isBlank(key)){
            model.addAttribute("posts",null);
            model.addAttribute("key","NULL");
        }else{
            try {
                Page<Post> posts = luceneService.findAll(key,page());
                model.addAttribute("posts",posts)
                     .addAttribute("offset",offset)
                     .addAttribute("key",key);
            }catch (Exception e){
                log.error(e.getMessage());
                model.addAttribute("posts",null);
                model.addAttribute("key",key);
            }
        }
        return blogView("/search.html");
    }

    @GetMapping(value = {"/about","/about.html","/me","/me.html"})
    public String about(){
        return blogView("/about.html");
    }


    @GetMapping("/blog/**")
    public String blog(HttpServletRequest request,Model model){
        String uri = request.getRequestURI();
        if(StringUtils.isBlank(uri)){
            throw new BlogNotFoundException("访问的博客不存在,uri="+uri);
        }
        String slug = uri.substring(uri.indexOf("blog/")+5);
        if(StringUtils.isBlank(slug)){
            throw new BlogNotFoundException("访问的博客不存在,slug="+slug);
        }
        log.info("blog slug is:{}",slug);
        Post post = postService.findBySlug(slug);
        if(post == null || post.getStatus() != UnaBootConst.ACTIVE){
            throw new BlogNotFoundException("访问的博客不存在或未发布，slug="+slug);
        }
        post.setVisits(visitsCache(post));
        PostVo blog = postService.convert(post);
        model.addAttribute("blog",blog);
        return blogView("/"+blog.getTheme());
    }

    @GetMapping("/404.html")
    public String notfound(){
        return blogView("/404.html");
    }

    private String blogView(String path){
        String theme = (String) servletContext.getAttribute("theme");
        if(path.endsWith(".html")){
            return "/"+theme+path.substring(0,path.indexOf("."));
        }else{
            return "/"+theme+path;
        }
    }

    private Long visitsCache(Post post){
        Ehcache ehcache = cacheManager.getEhcache("dayHits");
        Element element = ehcache.get(post.getId()+"_hits");
        long count = 0;
        if(element != null){
            count = (Long) element.getObjectValue();
        }
        count+=1;
        ehcache.put(new Element(post.getId()+"_hits",count));
        if(count >= 50){
            post.setVisits(post.getVisits()+count);
            postService.update(post);
            ehcache.remove(post.getId()+"_hits");
        }
        return post.getVisits();
    }
}
