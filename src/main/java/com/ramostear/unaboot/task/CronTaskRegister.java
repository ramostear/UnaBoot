package com.ramostear.unaboot.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName CronTaskRegister
 * @Description 定时任务注册中心
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 18:46
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Component
public class CronTaskRegister implements DisposableBean {

    private final Map<Runnable,ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(15);

    @Autowired
    private TaskScheduler taskScheduler;

    public TaskScheduler getTaskScheduler(){
        return this.taskScheduler;
    }


    public void addCronTask(Runnable task,String cronExpression){
        addCronTask(new CronTask(task,cronExpression));
    }

    public void addCronTask(CronTask cronTask){
        if(cronTask != null){
            Runnable task = cronTask.getRunnable();
            if(this.scheduledTasks.containsKey(task)){
                removeCronTask(task);
            }
            this.scheduledTasks.put(task,scheduledTask(cronTask));
        }
    }

    public void removeCronTask(Runnable task){
        ScheduledTask scheduledTask = this.scheduledTasks.remove(task);
        if(scheduledTask != null){
            scheduledTask.cancel();
           log.info("Task has been removed:{}",task.toString());
        }
    }
    public void removeAll(){
        Set<Runnable> runnables = this.scheduledTasks.keySet();
        if(runnables.size() > 0){
            runnables.forEach(this::removeCronTask);
        }
    }

    public ScheduledTask scheduledTask(CronTask cronTask){
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(),cronTask.getTrigger());
        return scheduledTask;
    }


    @Override
    public void destroy() throws Exception {

    }
}
