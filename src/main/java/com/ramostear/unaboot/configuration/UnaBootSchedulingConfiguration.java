package com.ramostear.unaboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @ClassName UnaBootSchedulingConfiguration
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 18:08
 * @Version since UnaBoot-1.0
 **/
@Configuration
public class UnaBootSchedulingConfiguration {

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("UnaBootTaskSchedulerThreadPool-");
        return taskScheduler;
    }

}
