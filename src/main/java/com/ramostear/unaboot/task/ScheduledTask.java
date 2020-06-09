package com.ramostear.unaboot.task;

import java.util.concurrent.ScheduledFuture;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 14:42.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public final class ScheduledTask {

    public volatile ScheduledFuture<?> future;

    public void cancel(){
        ScheduledFuture<?> future = this.future;
        if(future != null){
            future.cancel(true);
        }
    }
}
