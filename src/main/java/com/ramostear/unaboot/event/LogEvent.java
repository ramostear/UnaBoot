package com.ramostear.unaboot.event;

import com.ramostear.unaboot.domain.entity.OperLog;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/7/22 0022 13:09.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class LogEvent extends ApplicationEvent {

    private OperLog log;
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public LogEvent(Object source, OperLog log) {
        super(source);
        this.log = log;
    }

    public OperLog getLog() {
        return log;
    }
}
