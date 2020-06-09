package com.ramostear.unaboot.component;

import com.ramostear.unaboot.task.ScheduledTask;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 15:03.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component("taskRegister")
public class TaskRegister implements DisposableBean {

    private final Map<Runnable, ScheduledTask> scheduledTaskMap = new ConcurrentHashMap<>(15);

    @Autowired
    private TaskScheduler taskScheduler;

    public TaskScheduler getTaskScheduler(){
        return this.taskScheduler;
    }

    public void addCronTask(Runnable task,String cronExp){
        addCronTask(new CronTask(task,cronExp));
    }

    public void addCronTask(CronTask cronTask){
        if(cronTask != null){
            Runnable runnable = cronTask.getRunnable();
            if(this.scheduledTaskMap.containsKey(runnable)){
                removeCronTask(runnable);
            }
            this.scheduledTaskMap.put(runnable,scheduledTask(cronTask));
        }
    }

    public void removeCronTask(Runnable runnable){
        ScheduledTask task = this.scheduledTaskMap.remove(runnable);
        if(task != null){
            task.cancel();
        }
    }

    public void removeAll(){
        Set<Runnable> runnables = this.scheduledTaskMap.keySet();
        if(runnables.size() > 0){
            runnables.forEach(this::removeCronTask);
        }
    }

    public ScheduledTask scheduledTask(CronTask cronTask){
        ScheduledTask task = new ScheduledTask();
        task.future = this.taskScheduler.schedule(cronTask.getRunnable(),cronTask.getTrigger());
        return task;
    }

    @Override
    public void destroy() throws Exception {

    }
}
