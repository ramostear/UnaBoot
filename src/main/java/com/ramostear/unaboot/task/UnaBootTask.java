package com.ramostear.unaboot.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName UnaBootTask
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 19:48
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Component("unaBootTask")
public class UnaBootTask {

    public void demoTask(){
      log.info("无参数的示例任务");
    }

    public void demoTask(String params){
        log.info("有参数的示例任务:[{}]",params);
    }
}
