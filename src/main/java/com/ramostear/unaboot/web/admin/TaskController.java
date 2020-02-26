package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.domain.entity.UnaBootJob;
import com.ramostear.unaboot.service.JobService;
import com.ramostear.unaboot.task.CronTaskRegister;
import com.ramostear.unaboot.task.TaskSchedulingRunnable;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/index")
    public String index(Model model){
        Page<UnaBootJob> data = jobService.findAllJobs(page());
        model.addAttribute("data",data);
        return "/admin/task/index";
    }


}
