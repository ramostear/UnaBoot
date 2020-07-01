package com.ramostear.unaboot.web.front;

import com.ramostear.unaboot.common.PostStatus;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.domain.vo.PostVo;
import com.ramostear.unaboot.exception.NotFoundException;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.service.LuceneService;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.service.TagService;
import com.ramostear.unaboot.web.UnaBootController;
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
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 2:19.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
public class HomePageController extends UnaBootController {

    private ServletContext servletContext;

    private CategoryService categoryService;

    private TagService tagService;

    private PostService postService;

    private LuceneService luceneService;

    private static CacheManager CACHEMANAGER = CacheManager.newInstance();

    @Autowired
    HomePageController(ServletContext servletContext,CategoryService categoryService,
                       TagService tagService,PostService postService,LuceneService luceneService){
        this.tagService = tagService;
        this.postService = postService;
        this.luceneService = luceneService;
        this.servletContext = servletContext;
        this.categoryService = categoryService;
    }


    @GetMapping(value = {"/","/index","/index.html","/home","/home.html"})
    public String homePage(@RequestParam(name = "offset",defaultValue = "1")int offset, Model model){
        model.addAttribute("offset",offset);
        return themeView("/index.html");
    }

    @GetMapping("/category/{slug}")
    public String category(@PathVariable("slug")String slug,
                           @RequestParam(name = "offset",defaultValue = "1")int offset,
                           Model model){
        Category category = categoryService.findBySlug(slug);
        if(category != null){
            model.addAttribute("category",category)
                 .addAttribute("slug",slug)
                 .addAttribute("offset",offset);
            return themeView(category.getTheme());
        }else{
            throw new NotFoundException("你访问的栏目不存在");
        }
    }

    @GetMapping("/tags")
    public String tags(@RequestParam(name = "offset",defaultValue = "1")int offset,Model model){
        model.addAttribute("offset",offset);
        return themeView("/tags.html");
    }

    @GetMapping("/tags/{name}")
    public String tag(@PathVariable("name")String name,
                      @RequestParam(name = "offset",defaultValue = "1")int offset,
                      Model model){
        Tag tag = tagService.findBySlug(name);
        if(tag != null){
            model.addAttribute("tag",tag)
                 .addAttribute("offset",offset);
            return themeView("/tag.html");
        }else{
            throw new NotFoundException("标签不存在");
        }
    }

    @GetMapping("/archives")
    public String archives(@RequestParam(name = "offset",defaultValue = "1")int offset,Model model){
        model.addAttribute("offset",offset);
        return themeView("/archives.html");
    }

    @GetMapping("/archives/{name}")
    public String archive(@PathVariable("name")String name,@RequestParam(name = "offset",defaultValue = "1")int offset,Model model){
        if(StringUtils.isBlank(name)){
            throw new NotFoundException("归档不存在");
        }else{
            model.addAttribute("archive",name);
            model.addAttribute("offset",offset);
            return themeView("/archive.html");
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "key",defaultValue = "",required = false)String key,
                         @RequestParam(name = "offset",defaultValue = "1")int offset,Model model){
        model.addAttribute("offset",offset);
        if(StringUtils.isBlank(key)){
            model.addAttribute("posts",null);
            model.addAttribute("key","");
        }else{
            try{
                Page<Post> posts = luceneService.findAll(key,pageable());
                model.addAttribute("posts",posts)
                     .addAttribute("key",key);
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("posts",null)
                     .addAttribute("key",key);
            }
        }
        return themeView("/search.html");
    }

    @GetMapping(value = {"/about","/about.html","/me","/me.html"})
    public String about(){
        return themeView("/about.html");
    }

    @GetMapping("/404.html")
    public String notFound(){
        return themeView("/404.html");
    }

    @GetMapping("/posts/**")
    public String post(HttpServletRequest request,Model model){
        String uri = request.getRequestURI();
        if(StringUtils.isBlank(uri)){
            throw new NotFoundException("文章不存在");
        }
        String slug = uri.substring(uri.indexOf("posts/")+6);
        if(StringUtils.isBlank(slug)){
            throw new NotFoundException("文章不存在");
        }
        Post post = postService.findBySlug(slug);
        if(post == null || post.getStatus() != PostStatus.ACTIVE){
            throw new NotFoundException("文章不存在");
        }
        post.setVisits(visitsCache(post));
        PostVo vo = postService.valueOf(post);
        model.addAttribute("post",vo);
        String template = vo.getTpl();
        if(StringUtils.isBlank(template)){
            template = "/post.html";
        }
        return themeView(template);
    }


    private String themeView(String path){
        String theme = (String) servletContext.getAttribute("theme");
        if(path.startsWith("/")){
            if(path.endsWith(".html")){
                return "/"+theme+path.substring(0,path.indexOf("."));
            }else{
                return "/"+theme+path;
            }
        }else{
            if(path.endsWith(".html")){
                return "/"+theme+"/"+path.substring(0,path.indexOf("."));
            }else{
                return "/"+theme+"/"+path;
            }
        }
    }

    private Long visitsCache(Post post){
        Ehcache ehcache = CACHEMANAGER.getEhcache("dayHits");
        Element element = ehcache.get(post.getId()+"_hits");
        long count = 0;
        if(element != null){
            count = (Long)element.getObjectValue();
        }
        count+=1;
        ehcache.put(new Element(post.getId()+"_hits",count));
        if(count >=50){
            post.setVisits(post.getVisits()+count);
            postService.update(post);
            ehcache.remove(post.getId()+"_hits");
        }
        return post.getVisits();
    }
}
