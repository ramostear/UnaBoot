package com.ramostear.unaboot.task;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.UnaBootJob;
import com.ramostear.unaboot.service.JobService;
import com.ramostear.unaboot.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @ClassName UnaBootTask
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 19:48
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service("unaBootTask")
public class UnaBootTask {

    @Autowired
    private PostService postService;
    @Autowired
    private JobService jobService;
    @Autowired
    private CronTaskRegister cronTaskRegister;

    /**
     * 定时发布文章
     * @param params    文章序号
     */
    public void publishPostTask(String params){
        Integer postId = Integer.valueOf(params);
        log.info("task params:{}",postId);
        Post post = postService.findById(postId);
        if(post != null && post.getStatus() == UnaBootConst.WAIT){
            post.setStatus(UnaBootConst.ACTIVE);
            postService.update(post);
            UnaBootJob job = jobService.findByParam(params);
            if(job != null){
                job.setJobState(false);
                jobService.editJob(job);
                TaskSchedulingRunnable runnable = new TaskSchedulingRunnable("unaBootTask","publishPostTask",params);
                cronTaskRegister.removeCronTask(runnable);
            }
        }
    }

    /**
     * 重建构建系统任务
     */
    public void rebuildTask(){

    }
}
