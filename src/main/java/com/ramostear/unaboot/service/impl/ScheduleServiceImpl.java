package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.PostStatus;
import com.ramostear.unaboot.common.TaskMethods;
import com.ramostear.unaboot.component.TaskRegister;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.Schedule;
import com.ramostear.unaboot.repository.ScheduleRepository;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.service.ScheduleService;
import com.ramostear.unaboot.task.SchedulingRunner;
import com.ramostear.unaboot.util.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 15:49.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("scheduleService")
public class ScheduleServiceImpl extends BaseServiceImpl<Schedule,Integer> implements ScheduleService {

    private static final String TASK_BEAN_NAME = "taskService";
    private final ScheduleRepository scheduleRepository;
    private final TaskRegister taskRegister;
    private final PostService postService;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,TaskRegister taskRegister,
                               PostService postService) {
        super(scheduleRepository);
        this.scheduleRepository = scheduleRepository;
        this.taskRegister = taskRegister;
        this.postService = postService;
    }

    @Override
    @Transactional
    public Schedule create(Schedule schedule) {
        schedule.setBean(TASK_BEAN_NAME);
        if(StringUtils.isBlank(schedule.getParams())){
            schedule.setParams(schedule.getCronExp());
        }
        scheduleRepository.save(schedule);
        if(schedule.isState()){
            SchedulingRunner runner = new SchedulingRunner(schedule.getBean(),schedule.getMethod(),schedule.getCronExp(),schedule.getParams());
            taskRegister.addCronTask(runner,schedule.getCronExp());
        }
        return schedule;
    }

    @Override
    public Schedule update(Schedule schedule) {
        Schedule original = scheduleRepository.findById(schedule.getId()).orElse(null);
        if(original != null){
            BeanUtils.copyProperties(schedule,original,"id","createTime","creator");
            original.setUpdateTime(DateTimeUtils.now());
            scheduleRepository.save(original);
            SchedulingRunner runner = new SchedulingRunner(original.getBean(),original.getMethod(),original.getCronExp(),original.getParams());
            if(original.isState()){
                taskRegister.addCronTask(runner,original.getCronExp());
            }else{
                taskRegister.removeCronTask(runner);
            }
            return original;
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public Schedule delete(Integer id) {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);
        if(schedule != null){
            SchedulingRunner runner = new SchedulingRunner(schedule.getBean(),schedule.getMethod(),schedule.getCronExp(),schedule.getParams());
            taskRegister.removeCronTask(runner);
            scheduleRepository.delete(schedule);
            if(schedule.getMethod().equals(TaskMethods.PUBLISH_POST.getName())){
                Post post = postService.findById(Integer.parseInt(schedule.getParams()));
                if(post != null){
                    post.setStatus(PostStatus.WAIT);
                    postService.update(post);
                }
            }
            return schedule;
        }
        return null;
    }

    @Override
    public Schedule findByMethodAndCron(String method, String cronExp) {
        return scheduleRepository.findByMethodAndCronExp(method,cronExp);
    }

    @Override
    public Schedule findByParams(String params) {
        return scheduleRepository.findByParams(params);
    }

    @Override
    public Page<Schedule> findAll(Pageable pageable) {
        return scheduleRepository.findAllByOrderByUpdateTimeDesc(pageable);
    }

    @Override
    public void reloadAll() {
        List<Schedule> schedules = scheduleRepository.findAllByState(true);
        if(!CollectionUtils.isEmpty(schedules)){
            schedules.forEach(schedule->{
                SchedulingRunner runner = new SchedulingRunner(schedule.getBean(),schedule.getMethod(),schedule.getCronExp(),schedule.getParams());
                taskRegister.addCronTask(runner,schedule.getCronExp());
            });
        }
    }
}
