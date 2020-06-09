package com.ramostear.unaboot.util;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 4:59.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class DateTimeUtils {
    private DateTimeUtils(){}

    /**
     * Get current datetime.
     * @return current datetime
     */
    public static Date now(){
        return new Date();
    }


    public static Date append(Date current, long time, TimeUnit unit){
        Date newDate;
        int timeIntValue;
        if(time > Integer.MAX_VALUE){
            timeIntValue = Integer.MAX_VALUE;
        }else{
            timeIntValue = Long.valueOf(time).intValue();
        }

        switch (unit){
            case DAYS:
                newDate = DateUtils.addDays(current,timeIntValue);
                break;
            case HOURS:
                newDate = DateUtils.addHours(current,timeIntValue);
                break;
            case MINUTES:
                newDate = DateUtils.addMinutes(current,timeIntValue);
                break;
            case SECONDS:
                newDate = DateUtils.addSeconds(current,timeIntValue);
                break;
            case MICROSECONDS:
                newDate = DateUtils.addMilliseconds(current,timeIntValue);
                break;
            default:
                newDate = current;
        }
        return newDate;
    }
}
