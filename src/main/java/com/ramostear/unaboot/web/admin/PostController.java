package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.exception.UnaBootException;
import com.ramostear.unaboot.common.util.CronUtils;
import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.common.util.RandomUtils;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.UnaBootJob;
import com.ramostear.unaboot.domain.param.PostParam;
import com.ramostear.unaboot.domain.valueobject.PostQuery;
import com.ramostear.unaboot.domain.valueobject.TaskVo;
import com.ramostear.unaboot.service.*;
import com.ramostear.unaboot.task.CronTaskRegister;
import com.ramostear.unaboot.task.TaskSchedulingRunnable;
import com.ramostear.unaboot.web.UnaBootController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private PostTagService postTagService;
    @Autowired
    private PostCategoryService postCategoryService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private JobService jobService;
    @Autowired
    private CronTaskRegister cronTaskRegister;

    @Autowired
    private ServletContext servletContext;


    @GetMapping("/index")
    public String index(PostQuery query, Model model){
        Page<Post> posts = postService.pageBy(query,pageByDesc("createTime"));
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

    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<Object> write(@Valid @RequestBody PostParam param, BindingResult bindingResult,@RequestParam(value = "autoSave",defaultValue = "0",required = false)int  autoSave){
        if(bindingResult.hasFieldErrors()){
            return badRequest("服务端字段校验未通过");
        }
        try{
            Post post = param.convertTo();
            postService.createBy(post,param.tagArray(),param.getCategory(),autoSave==1);
            log.info("Save post data into database has completed:{}",post.getTitle());
            return ok("文章已经保存成功");
        }catch (UnaBootException e){
            log.error("Save post data into database error:{}",e.getMessage());
            return badRequest("服务器异常，请稍后再试!");
        }
    }

    @GetMapping("/{id:\\d+}/publish")
    public String publish(@PathVariable("id")Integer id,Model model){
        model.addAttribute("id",id);
        model.addAttribute("defaultDate",DateTimeUtils.append(new Date(),1, TimeUnit.DAYS));
        return "/admin/post/publish";
    }

    /**
     * 通过定时任务发布文章
     * @param id        文章ID
     * @param taskVo    发布类型和发布时间
     * @return
     */
    @PostMapping("/{id:\\d+}/publish")
    @ResponseBody
    public ResponseEntity<Object> publish(@PathVariable("id")Integer id, TaskVo taskVo){
        Post post = postService.findById(id);
        if(post == null || post.getStatus().equals(UnaBootConst.ACTIVE)){
            return badRequest();
        }
        if(taskVo.getType()==1){
            post.setStatus(UnaBootConst.ACTIVE);
            postService.update(post);
            return ok();
        }
        UnaBootJob job = jobService.findByParam(String.valueOf(id));
        if(job != null){
            job.setJobState(true);
            job.setCronTime(taskVo.getPublishDate());
            job.setCronExpression(CronUtils.getCron(taskVo.getPublishDate()));
            job.setUpdateTime(DateTimeUtils.current());
        }else{
            job = new UnaBootJob();
            job.setCronExpression(CronUtils.getCron(taskVo.getPublishDate()));
            job.setCronTime(taskVo.getPublishDate());
            job.setJobState(true);
            job.setRemark("定时发布文章："+post.getTitle());
            job.setParams(String.valueOf(id));
            job.setBeanName("unaBootTask");
            job.setMethodName("publishPostTask");
        }
        boolean flag = false;
        jobService.addJob(job);
        if(job.getJobId() > 0){
            if(job.getJobState()){
                TaskSchedulingRunnable task = new TaskSchedulingRunnable(job.getBeanName(),job.getMethodName(),job.getParams());
                cronTaskRegister.addCronTask(task,job.getCronExpression());
                post.setStatus(UnaBootConst.WAIT);
                postService.update(post);
                flag = true;
            }
        }
        if(flag){
            return ok();
        }else{
            return badRequest();
        }
    }

    @GetMapping("/{id:\\d+}")
    public String update(@PathVariable("id")Integer id,Model model){
        String theme = (String) servletContext.getAttribute("theme");
        if(StringUtils.isEmpty(theme)){
            theme = "default";
        }
        Post post = postService.findById(id);
        model.addAttribute("post",postService.convert(post))
                .addAttribute("themes",themeService.templateDetail(theme))
                .addAttribute("categories",categoryService.findAll());
        return "/admin/post/edit";
    }

    @PostMapping("/{id:\\d+}")
    @ResponseBody
    public ResponseEntity<Object> update(@Valid @RequestBody PostParam param,BindingResult bindingResult,@PathVariable("id")Integer id,@RequestParam(value = "autoSave",required = false,defaultValue = "0")int autoSave){
        if(bindingResult.hasFieldErrors()){
            return badRequest("服务端字段校验未通过");
        }
        try{
            Post post = postService.findById(id);
            if(post != null){
                param.update(post);
                postService.updateBy(post,param.tagArray(),param.getCategory(),autoSave == 1);
                return ok("文章已经修改成功");
            }else{
                return badRequest("不存在编号为："+id+" 的文章");
            }
        }catch (UnaBootException e){
            log.error("Update post data error:{}",e.getMessage());
            return badRequest("服务器异常，请稍后再试!");
        }
    }

    @DeleteMapping("/{id:\\d+}")
    @ResponseBody
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        Post post = postService.findById(id);
        if(post == null ||post.getStatus() == UnaBootConst.WAIT){
            return badRequest("文章不存在或正在发布中，暂不予删除！");
        }
        try {
            postService.delete(post);
            postTagService.removeByPost(id);
            postCategoryService.removeByPostId(id);
        }catch (UnaBootException e){
            log.error("Delete post from database error:{}",e.getMessage());
            return badRequest("服务器异常，请稍后再操作!");
        }
        return ok("文章已经删除");
    }

    @GetMapping("/thumb")
    public String thumb(){
        return "/admin/post/thumb";
    }
    @PostMapping("/thumb")
    @ResponseBody
    public ResponseEntity<Object> deleteThumb(@RequestParam(name = "url")String url){
        if(StringUtils.isBlank(url)){
            return badRequest();
        }else{
            boolean flag = uploadService.delete(url);
            if(flag){
                return ok();
            }else{
                return badRequest();
            }
        }
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
