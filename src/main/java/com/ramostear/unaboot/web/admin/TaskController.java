package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.domain.entity.UnaBootJob;
import com.ramostear.unaboot.service.JobService;
import com.ramostear.unaboot.task.CronTaskRegister;
import com.ramostear.unaboot.task.TaskSchedulingRunnable;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName TaskController
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 19:51
 * @Version since UnaBoot-1.0
 **/
@Controller
@RequestMapping("/admin/task")
public class TaskController extends UnaBootController {

    @Autowired
    private JobService jobService;

    @Autowired
    private CronTaskRegister cronTaskRegister;


    @GetMapping("/test")
    @ResponseBody
    public String test(){
        UnaBootJob job = new UnaBootJob();
        job.setBeanName("unaBootTask");
        job.setMethodName("demoTask");
        job.setParams("18645229963");
        job.setCronExpression("0/2 * * * * ?");
        job.setCronTime(DateTimeUtils.current());
        job.setJobState(true);
        job.setRemark("测试带参数的定时任务是否正常执行");

        jobService.addJob(job);
        if(job.getJobId() > 0){
            if(job.getJobState()){
                TaskSchedulingRunnable task = new TaskSchedulingRunnable(job.getBeanName(),job.getMethodName(),job.getParams());
                cronTaskRegister.addCronTask(task,job.getCronExpression());
            }
        }else{
            return "Execute UnaBoot Task Failure.";
        }
        return "Execute UnaBoot Task Successfully!";
    }
}
