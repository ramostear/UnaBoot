package com.ramostear.unaboot.event.listener;

import com.ramostear.unaboot.domain.entity.OperLog;
import com.ramostear.unaboot.event.LogEvent;
import com.ramostear.unaboot.service.OperLogService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/7/22 0022 13:11.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component
public class LogEventListener {

    private final OperLogService operLogService;

    LogEventListener(OperLogService operLogService){
        this.operLogService = operLogService;
    }

    @EventListener
    public void saveLog(LogEvent event){
        OperLog log = event.getLog();
        operLogService.create(log);
    }

}
