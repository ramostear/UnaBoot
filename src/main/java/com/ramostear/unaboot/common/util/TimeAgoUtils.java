package com.ramostear.unaboot.common.util;

import java.util.Date;

/**
 * @ClassName TimeAgoUtils
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 22:09
 * @Version since UnaBoot-1.0
 **/
public class TimeAgoUtils {

    private TimeAgoUtils(){}

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = " 秒以前";
    private static final String ONE_MINUTE_AGO = " 分钟以前";
    private static final String ONE_HOUR_AGO = " 小时以前";
    private static final String ONE_DAY_AGO = " 天以前";
    private static final String ONE_MONTH_AGO = " 月以前";
    private static final String ONE_YEAR_AGO = " 年以前";
    private static final String ONE_UNKNOWN = " 很久以前";

    public static String format(Date date) {
        if (null == date) {
            return ONE_UNKNOWN;
        }
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    public static long toSeconds(long date) {
        return date / 1000L;
    }

    public static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    public static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    public static long toDays(long date) {
        return toHours(date) / 24L;
    }

    public static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    public static long toYears(long date) {
        return toMonths(date) / 365L;
    }
}
