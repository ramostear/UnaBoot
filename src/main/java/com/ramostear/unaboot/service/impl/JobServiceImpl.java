package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.UnaBootJob;
import com.ramostear.unaboot.repository.UnaBootJobRepository;
import com.ramostear.unaboot.service.JobService;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import com.ramostear.unaboot.task.CronTaskRegister;
import com.ramostear.unaboot.task.TaskSchedulingRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName JobServiceImpl
 * @Description 定时任务服务
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 19:28
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service("jobService")
public class JobServiceImpl extends UnaBootServiceImpl<UnaBootJob,Integer> implements JobService {

    private final UnaBootJobRepository unaBootJobRepository;
    private final CronTaskRegister cronTaskRegister;
    private final PostService postService;

    public JobServiceImpl(UnaBootJobRepository unaBootJobRepository,CronTaskRegister cronTaskRegister,PostService postService) {
        super(unaBootJobRepository);
        this.unaBootJobRepository = unaBootJobRepository;
        this.cronTaskRegister = cronTaskRegister;
        this.postService = postService;
    }

    @Override
    @Transactional
    public UnaBootJob addJob(UnaBootJob job) {
        job.setBeanName("unaBootTask");
        if(StringUtils.isEmpty(job.getParams())){
            job.setParams(job.getCronExpression());
        }
        unaBootJobRepository.save(job);
        if(job.getJobState()){
            TaskSchedulingRunnable runnable = new TaskSchedulingRunnable(job.getBeanName(),job.getMethodName(),job.getCronExpression(),job.getParams());
            cronTaskRegister.addCronTask(runnable,job.getCronExpression());
        }
        return job;
    }

    @Override
    @Transactional
    public boolean onlyRemoveJob(UnaBootJob job) {
        Optional<UnaBootJob> jobOptional = unaBootJobRepository.findById(job.getJobId());
        if(jobOptional.isPresent()){
            UnaBootJob task = jobOptional.get();
            TaskSchedulingRunnable runnable = new TaskSchedulingRunnable(job.getBeanName(),job.getMethodName(),job.getCronExpression(),job.getParams());
            cronTaskRegister.removeCronTask(runnable);
            delete(task);
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional
    public boolean removeJob(Integer jobId) {
        Optional<UnaBootJob> jobOptional = unaBootJobRepository.findById(jobId);
        if(jobOptional.isPresent()){
          UnaBootJob job = jobOptional.get();
          TaskSchedulingRunnable runnable = new TaskSchedulingRunnable(job.getBeanName(),job.getMethodName(),job.getCronExpression(),job.getParams());
          cronTaskRegister.removeCronTask(runnable);
          delete(job);
          if(job.getMethodName().equals("publishPostTask")){
              Post post = postService.findById(Integer.parseInt(job.getParams()));
              if(post != null){
                  post.setStatus(UnaBootConst.DRAFT);
                  postService.update(post);
              }
          }
          return true;
        }else{
            return false;
        }
    }

    @Override
    public UnaBootJob getJob(Integer jobId) {
        return findById(jobId);
    }

    @Override
    public UnaBootJob findByParam(String param) {
        return unaBootJobRepository.findByParams(param);
    }

    @Override
    public UnaBootJob findByMethodNameAndCron(String methodName, String cronExpression) {
        return null;
    }

    @Override
    @Transactional
    public boolean editJob(UnaBootJob job) {
        UnaBootJob original = getJob(job.getJobId());
        if(original != null){
            BeanUtils.copyProperties(job,original,"jobId","createTime");
            original.setUpdateTime(DateTimeUtils.current());
            unaBootJobRepository.save(original);
            TaskSchedulingRunnable runnable = new TaskSchedulingRunnable(job.getBeanName(),job.getMethodName(),job.getCronExpression(),job.getParams());
            if(original.getJobState()){
                cronTaskRegister.addCronTask(runnable,job.getCronExpression());
            }else{
                cronTaskRegister.removeCronTask(runnable);
            }
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Page<UnaBootJob> findAllJobs(Pageable pageable) {
        return unaBootJobRepository.findAllByOrderByUpdateTimeDesc(pageable);
    }

    @Override
    public void reloadAll() {
        List<UnaBootJob> jobs = unaBootJobRepository.findAllByJobState(true);
        if(!CollectionUtils.isEmpty(jobs)){
            jobs.forEach(job -> {
                TaskSchedulingRunnable task = new TaskSchedulingRunnable(job.getBeanName(),job.getMethodName(),job.getCronExpression(),job.getParams());
                cronTaskRegister.addCronTask(task,job.getCronExpression());
                log.info("Reload job info:{}",job.toString());
            });
        }
    }
}
