package com.ramostear.unaboot.task;

import java.util.concurrent.ScheduledFuture;

/**
 * @ClassName ScheduledTask
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 18:11
 * @Version since UnaBoot-1.0
 **/
public final class ScheduledTask {
    volatile ScheduledFuture<?> future;

    public void cancel(){
        ScheduledFuture<?> future = this.future;
        if(future != null){
            future.cancel(true);
        }
    }
}
