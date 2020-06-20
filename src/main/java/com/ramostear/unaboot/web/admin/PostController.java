package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.common.PostStatus;
import com.ramostear.unaboot.common.SortType;
import com.ramostear.unaboot.common.TaskMethods;
import com.ramostear.unaboot.component.FileManager;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.Schedule;
import com.ramostear.unaboot.domain.param.PostParam;
import com.ramostear.unaboot.domain.vo.QueryParam;
import com.ramostear.unaboot.domain.vo.ScheduleVo;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.*;
import com.ramostear.unaboot.util.DateTimeUtils;
import com.ramostear.unaboot.util.HtmlUtils;
import com.ramostear.unaboot.util.UnaBootUtils;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private final UserService userService;
    private final ScheduleService scheduleService;
    private final PostTagService postTagService;
    private final PostCategoryService postCategoryService;
    private final FileManager fileManager;


    @Autowired
    public PostController(PostService postService,CategoryService categoryService,
                          UserService userService,ScheduleService scheduleService,
                          PostTagService postTagService,PostCategoryService postCategoryService,
                          FileManager fileManager){
        this.postService = postService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.postTagService = postTagService;
        this.postCategoryService = postCategoryService;
        this.fileManager = fileManager;
    }


    @GetMapping("/")
    public String posts(QueryParam param, Model model){
        Page<Post> data = postService.page(param,pageable("createTime", SortType.DESC));
        model.addAttribute("data",postService.valueOf(data))
             .addAttribute("param",param)
             .addAttribute("urlParam",urlParam(param))
             .addAttribute("categories",categoryService.findAll())
             .addAttribute("users",userService.findAll());
        return "/admin/post/list";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("slug",initializeSlug())
             .addAttribute("categories",categoryService.findAll());//TODO 需要区分人员
        return "/admin/post/create";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody PostParam param, BindingResult br){
        if(br.hasFieldErrors()){
            return bad("数据未通过校验");
        }
        try {
            Post post = param.convertTo();
            postService.createBy(post,param.tags(),param.getCategoryId(),param.getStatus());
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @GetMapping("/{id:\\d+}/publish")
    public String publish(@PathVariable("id")Integer id,Model model){
        model.addAttribute("postId",id)
             .addAttribute("publishDate",DateTimeUtils.append(new Date(),1, TimeUnit.DAYS));
        return "/admin/post/publish";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}/publish")
    public ResponseEntity<Object> publish(@PathVariable("id")Integer id, ScheduleVo scheduleVo){
        try {
            Post post = postService.findById(id);
            if(post == null || post.getStatus() == PostStatus.ACTIVE){
                return bad();
            }
            if(scheduleVo.getType() == 1){
                post.setStatus(PostStatus.ACTIVE);
                postService.update(post);
                return ok();
            }
            Schedule schedule = initializeSchedule(id,scheduleVo);
            scheduleService.create(schedule);
            post.setStatus(PostStatus.SCHEDULE);
            postService.update(post);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @GetMapping("/{id:\\d+}")
    public String post(@PathVariable("id")Integer id,Model model){
        Post post = postService.findById(id);
        model.addAttribute("post",postService.valueOf(post))
             .addAttribute("categories",categoryService.findAll());
        return "/admin/post/view";
    }

    @PostMapping("/{id:\\d+}")
    @ResponseBody
    public ResponseEntity<Object> post(@PathVariable("id")Integer id,@Valid @RequestBody PostParam param,BindingResult br){
        if(br.hasFieldErrors()){
            return bad("数据未通过校验");
        }
        try {
            Post post = postService.findById(id);
            if(post != null){
                param.updateTo(post);
                postService.updateBy(post,param.tags(),param.getCategoryId(),param.getStatus());
                return ok();
            }else{
                return bad();
            }
        }catch (UnaBootException e){
            return bad();
        }
    }

    @ResponseBody
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        Post post = postService.findById(id);
        if(post == null || post.getStatus() == PostStatus.WAIT){
            return bad();
        }
        try {
            List<String> imgUrls = HtmlUtils.getAllImgUrl(post.getContent());
            if(StringUtils.isNotBlank(post.getThumb())){
                imgUrls.add(post.getThumb());
            }
            postTagService.removeByPostId(post.getId());
            postCategoryService.removeByPostId(post.getId());
            postService.delete(post);
            fileManager.remove(imgUrls);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @GetMapping("/thumb")
    public String thumb(){
        return "/admin/post/thumb";
    }

    @PostMapping("/thumb")
    @ResponseBody
    public ResponseEntity<Object> removeThumb(@RequestParam("name=url")String url){
        if(StringUtils.isBlank(url)){
            return bad();
        }else{
            boolean result = fileManager.remove(url);
            return result?ok():bad();
        }
    }

    private Schedule initializeSchedule(Integer postId,ScheduleVo scheduleVo){
        Schedule schedule = scheduleService.findByParams(String.valueOf(postId));
        if(schedule != null){
            schedule.setState(true);
            schedule.setCronExp(UnaBootUtils.toCronExp(scheduleVo.getPublishDate()));
            schedule.setUpdateTime(DateTimeUtils.now());
        }else{
            schedule = new Schedule();
            schedule.setCronExp(UnaBootUtils.toCronExp(scheduleVo.getPublishDate()));
            schedule.setCreateTime(DateTimeUtils.now());
            schedule.setUpdateTime(DateTimeUtils.now());
            schedule.setState(true);
            schedule.setIntroduce("定时发布编号为"+postId+"的文章，Cron表达式为："+schedule.getCronExp());
            schedule.setParams(String.valueOf(postId));
            schedule.setBean(Constants.TASK_BEAN_NAME);
            schedule.setMethod(TaskMethods.PUBLISH_POST.getName());
        }
        return schedule;
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
