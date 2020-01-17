package com.ramostear.unaboot.common.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.util.Assert;
import org.springframework.lang.NonNull;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DateTimeUtils
 * @Description 日期工具类
 * @Author 树下魅狐
 * @Date 2020/1/17 0017 4:18
 * @Version since UnaBoot-1.0
 **/
public class DateTimeUtils {

    private DateTimeUtils(){}

    /**
     * 获取当前时间
     * @return
     */
    @NonNull
    public static Date current(){
        return new Date();
    }

    /**
     * 将日期转换为日历
     * @param date
     * @return
     */
    @NonNull
    public static Calendar convertTo(@NonNull Date date){
        Assert.notNull(date,"Date must be not null");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 将时间转换为字符串
     * @param date      时间
     * @param pattern   格式
     * @return
     */
    public static String convertTo(Date date,String pattern){
        if(ObjectUtils.allNotNull(date)){
            return new SimpleDateFormat(pattern).format(date);
        }else{
            return null;
        }
    }

    /**
     * 获取倒计时字符串
     * @param endTime       截止时间
     * @param current       当前时间
     * @return
     */
    public static String getDatePoor(Date endTime,Date current){
        long nd = (long)1000*24*60*60;
        long nh = (long)1000*60*60;
        long nm = (long)1000*60;
        long diff = endTime.getTime() - current.getTime();
        long day = diff/nd;
        long hour = diff % nd/nh;
        long min = diff % nd % nh / nm;
        return day+"天"+hour+"小时"+min+"分钟";
    }

    /**
     * 给当前日期追加时间
     * @param current 当前时间
     * @param time    追加时长
     * @param unit    时长单位
     * @return
     */
    public static Date append(@NonNull Date current, long time, @NonNull TimeUnit unit){
        Assert.notNull(current,"current date is null");
        Assert.isTrue(time > 0,"append time must not be less than 1");
        Assert.notNull(unit,"time unit must not be null");
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

    /**
     * 获取服务器启动时间
     * @return
     */
    public static Date getServerStqrtTime(){
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

}
