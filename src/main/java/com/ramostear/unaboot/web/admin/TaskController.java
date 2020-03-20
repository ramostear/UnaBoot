package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.domain.entity.UnaBootJob;
import com.ramostear.unaboot.domain.valueobject.TaskMethodVo;
import com.ramostear.unaboot.service.JobService;
import com.ramostear.unaboot.task.CronTaskRegister;
import com.ramostear.unaboot.task.TaskSchedulingRunnable;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TaskController
 * @Description 任务控制器
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 19:51
 * @Version since UnaBoot-1.0
 **/
@RequiresRoles(value = UnaBootConst.ROLE_ADMIN)
@Controller
@RequestMapping("/admin/task")
public class TaskController extends UnaBootController {

    @Autowired
    private JobService jobService;

    @Autowired
    private CronTaskRegister cronTaskRegister;

    private static List<TaskMethodVo> methods = new LinkedList<>();

    static {
        methods.add(TaskMethodVo.builder().zhName("定时发文").enName("publishPostTask").build());
        methods.add(TaskMethodVo.builder().zhName("重建索引").enName("rebuildSearchIndex").build());
        methods.add(TaskMethodVo.builder().zhName("清理缓存").enName("clearCache").build());
    }

    @GetMapping("/index")
    public String index(Model model){
        Page<UnaBootJob> data = jobService.findAllJobs(page());
        model.addAttribute("data",data);
        return "/admin/task/index";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("methods",methods);
        return "/admin/task/create";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(UnaBootJob unaBootJob){
        if(jobService.addJob(unaBootJob) != null){
            return ok();
        }else{
            return badRequest();
        }
    }

    @GetMapping("/{id:\\d+}")
    public String get(@PathVariable("id")Integer id,Model model){
        UnaBootJob job = jobService.getJob(id);
        model.addAttribute("job",job);
        model.addAttribute("methods",methods);
        return "/admin/task/edit";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}")
    public ResponseEntity<Object> update(@PathVariable("id")Integer id, UnaBootJob job){
        job.setJobId(id);
        if(!job.getMethodName().equals("publishPostTask")){
            job.setParams(job.getCronExpression());
        }
        if(jobService.editJob(job)){
            return ok();
        }else{
            return badRequest();
        }
    }

    @ResponseBody
    @PutMapping("/stop/{id:\\d+}")
    public ResponseEntity<Object> stopTask(@PathVariable("id")Integer id){
        UnaBootJob job = jobService.getJob(id);
        if(job != null){
            job.setJobState(false);
            jobService.editJob(job);
            return ok();
        }else{
            return badRequest();
        }
    }

    @ResponseBody
    @PutMapping("/start/{id:\\d+}")
    public ResponseEntity<Object> startTask(@PathVariable("id")Integer id){
        UnaBootJob job = jobService.getJob(id);
        if(job != null){
            job.setJobState(true);
            jobService.editJob(job);
            return ok();
        }else{
            return badRequest();
        }
    }


    @ResponseBody
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        boolean flag = jobService.removeJob(id);
        if(flag){
            return ok();
        }else{
            return badRequest();
        }
    }

}
