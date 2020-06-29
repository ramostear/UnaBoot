package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.*;
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
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
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
    private final UserCategoryService userCategoryService;



    @Autowired
    public PostController(PostService postService,CategoryService categoryService,
                          UserService userService,ScheduleService scheduleService,
                          PostTagService postTagService,PostCategoryService postCategoryService,
                          FileManager fileManager,UserCategoryService userCategoryService){
        this.postService = postService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.scheduleService = scheduleService;
        this.postTagService = postTagService;
        this.postCategoryService = postCategoryService;
        this.fileManager = fileManager;
        this.userCategoryService = userCategoryService;
    }

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
    @GetMapping("/")
    public String posts(QueryParam param, Model model){
        Page<Post> data;
        if(currentUser().getRole().equals(Authorized.ADMIN.getName())){
            data = postService.page(param,pageable("createTime", SortType.DESC));
        }else{
            data = postService.pageByUser(currentUser().getId(),pageable("createTime",SortType.DESC));
        }
        model.addAttribute("data",postService.valueOf(data));
        if(currentUser().getRole().equals(Authorized.ADMIN.getName())){
            model.addAttribute("param",param).addAttribute("urlParam",urlParam(param));
            model.addAttribute("users",userService.findAll());
            model.addAttribute("categories",categoryService.findAll());
            model.addAttribute("total",postService.totalCount());
            model.addAttribute("published",postService.countByStatus(PostStatus.ACTIVE));
        }else{
            List<Integer> categoryIds = userCategoryService.findAllCategoryByUserId(currentUser().getId());
            model.addAttribute("categories",categoryService.findAll(categoryIds));
            model.addAttribute("total",postService.countByUserId(currentUser().getId()));
            model.addAttribute("published",postService.countByUserIdAndStatus(currentUser().getId(),PostStatus.ACTIVE));
        }
        return "/admin/post/list";
    }

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
    @GetMapping("/actives")
    public String actives(QueryParam param,Model model){
        Page<Post> data;
        if(currentUser().getRole().equals(Authorized.ADMIN.getName())){
            data = postService.pageByStatus(PostStatus.ACTIVE,pageable("createTime",SortType.DESC));
        }else{
            data = postService.pageByUserAndStatus(currentUser().getId(),PostStatus.ACTIVE,pageable("createTime",SortType.DESC));
        }
        model.addAttribute("data",postService.valueOf(data));
        if(currentUser().getRole().equals(Authorized.ADMIN.getName())){
            model.addAttribute("param",param).addAttribute("urlParam",urlParam(param));
            model.addAttribute("users",userService.findAll());
            model.addAttribute("categories",categoryService.findAll());
            model.addAttribute("total",postService.totalCount());
            model.addAttribute("published",postService.countByStatus(PostStatus.ACTIVE));
        }else{
            List<Integer> categoryIds = userCategoryService.findAllCategoryByUserId(currentUser().getId());
            model.addAttribute("categories",categoryService.findAll(categoryIds));
            model.addAttribute("total",postService.countByUserId(currentUser().getId()));
            model.addAttribute("published",postService.countByUserIdAndStatus(currentUser().getId(),PostStatus.ACTIVE));
        }
        return "/admin/post/actives";
    }

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("slug",initializeSlug());
        if(currentUser().getRole().equals(Authorized.ADMIN.getName())){
            model.addAttribute("categories",categoryService.findAll());
        }else{
            List<Integer> categoryIds = userCategoryService.findAllCategoryByUserId(currentUser().getId());
            model.addAttribute("categories",categoryService.findAll(categoryIds));
        }
        return "/admin/post/create";
    }

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody PostParam param, BindingResult br){
        if(br.hasFieldErrors()){
            return bad("数据未通过校验");
        }
        try {
            if(StringUtils.isBlank(param.getAuthor())){
                param.setAuthor(currentUser().getUsername());
            }
            if(!currentUser().getRole().equals(Authorized.ADMIN.getName())){
                List<Integer> categoryIds = userCategoryService.findAllCategoryByUserId(currentUser().getId());
                if(CollectionUtils.isEmpty(categoryIds) || !categoryIds.contains(param.getCategoryId())){
                    return bad();
                }
            }
            Post post = param.convertTo();
            post.setUserId(currentUser().getId());
            postService.createBy(post,param.tags(),param.getCategoryId(),param.getStatus());
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
    @GetMapping("/{id:\\d+}/publish")
    public String publish(@PathVariable("id")Integer id,Model model){
        Schedule schedule = scheduleService.findByParams(String.valueOf(id));
        if(schedule != null){
            Date publishDate = UnaBootUtils.cronExpToDate(schedule.getCronExp());
            long current = System.currentTimeMillis();
            if(publishDate.getTime() < current){
                model.addAttribute("publishDate",DateTimeUtils.append(new Date(),1, TimeUnit.HOURS));
            }else{
                model.addAttribute("publishDate",publishDate);
            }
        }else{
            model.addAttribute("publishDate",DateTimeUtils.append(new Date(),1, TimeUnit.HOURS));
        }
        model.addAttribute("postId",id);
        return "/admin/post/publish";
    }

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
    @ResponseBody
    @PostMapping("/{id:\\d+}/publish")
    public ResponseEntity<Object> publish(@PathVariable("id")Integer id, ScheduleVo scheduleVo){
        try {
            Post post = postService.findById(id);
            if(post == null || post.getStatus() == PostStatus.ACTIVE){
                return bad();
            }
            if(currentUser().getRole().equals(Authorized.EDITOR.getName())){
                if(scheduleVo.getType() == 1){
                    post.setStatus(PostStatus.WAIT);
                    postService.update(post);
                    return ok();
                }
                Schedule schedule = initializeSchedule(id,post.getTitle(),scheduleVo);
                schedule.setState(false);
                scheduleService.createOrUpdate(schedule);
                post.setStatus(PostStatus.WAIT);
                postService.update(post);
                return ok();

            }else{
                if(scheduleVo.getType() == 1){
                    post.setStatus(PostStatus.ACTIVE);
                    postService.update(post);
                    return ok();
                }
                Schedule schedule = initializeSchedule(id,post.getTitle(),scheduleVo);
                scheduleService.createOrUpdate(schedule);
                post.setStatus(PostStatus.SCHEDULE);
                postService.update(post);
                return ok();
            }
        }catch (UnaBootException e){
            return bad();
        }
    }

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
    @GetMapping("/{id:\\d+}")
    public String post(@PathVariable("id")Integer id,Model model){
        Post post = postService.findById(id);
        if(post.getStatus() == PostStatus.DRAFT && !post.getUserId().equals(currentUser().getId())){
            return sendRedirect("/admin/posts/");
        }
        model.addAttribute("post",postService.valueOf(post));
        if(currentUser().getRole().equals(Authorized.ADMIN.getName())){
            model.addAttribute("categories",categoryService.findAll());
        }else{
            List<Integer> categoryIds = userCategoryService.findAllCategoryByUserId(currentUser().getId());
            model.addAttribute("categories",categoryService.findAll(categoryIds));
        }
        return "/admin/post/view";
    }

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
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

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
    @ResponseBody
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        Post post = postService.findById(id);
        if(post == null || post.getStatus() == PostStatus.WAIT){
            return bad();
        }
        if(post.getStatus() == PostStatus.DRAFT){
            if(!post.getUserId().equals(currentUser().getId())){
                return bad();
            }
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

    @RequiresUser
    @GetMapping("/thumb")
    public String thumb(){
        return "/admin/post/thumb";
    }

    @RequiresUser
    @PostMapping("/thumb")
    @ResponseBody
    public ResponseEntity<Object> removeThumb(@RequestParam("url")String url){
        if(StringUtils.isBlank(url)){
            return bad();
        }else{
            boolean result = fileManager.remove(url);
            return result?ok():bad();
        }
    }

    @RequiresRoles(value = "admin")
    @ResponseBody
    @PutMapping("/reject/{id:\\d+}")
    public ResponseEntity<Object> reject(@PathVariable("id")Integer id){
        Post post = postService.findById(id);
        if(post == null || post.getStatus() != PostStatus.WAIT){
            return bad();
        }
        post.setStatus(PostStatus.DRAFT);
        postService.update(post);
        return ok();
    }
    @RequiresRoles(value = "editor")
    @ResponseBody
    @PutMapping("/revoke/{id:\\d+}")
    public ResponseEntity<Object> revoke(@PathVariable("id")Integer id){
        Post post = postService.findById(id);
        if(post == null || post.getStatus() != PostStatus.WAIT || post.getUserId() != currentUser().getId()){
            return bad();
        }
        post.setStatus(PostStatus.DRAFT);
        postService.update(post);
        return ok();
    }

    @RequiresRoles(value = {"admin","editor"},logical = Logical.OR)
    @ResponseBody
    @PutMapping("/undo/{id:\\d+}")
    public ResponseEntity<Object> undo(@PathVariable("id")Integer id){
        Post post = postService.findById(id);
        if(post == null || post.getStatus() != PostStatus.ACTIVE){
            return bad();
        }
        post.setStatus(PostStatus.DRAFT);
        postService.update(post);
        return ok();
    }

    @GetMapping("/draft")
    public String draft(Model model){
        Page<Post> posts = postService.draft(currentUser().getId(),pageable("createTime",SortType.DESC));
        model.addAttribute("data",postService.valueOf(posts));
        return "/admin/post/draft";
    }

    private Schedule initializeSchedule(Integer postId,String title,ScheduleVo scheduleVo){
        Schedule schedule = scheduleService.findByParams(String.valueOf(postId));
        if(scheduleVo.getPublishDate().getTime() < DateTimeUtils.now().getTime()){
            scheduleVo.setPublishDate(DateTimeUtils.append(scheduleVo.getPublishDate(),1, TimeUnit.HOURS));
        }
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
            schedule.setCreator(currentUser().getUsername());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            schedule.setIntroduce("于"+sdf.format(scheduleVo.getPublishDate())+"发布名为《"+title+"》的文章");
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
        if(param.getUserId() > 0){
            sb.append("&userId=").append(param.getUserId());
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
