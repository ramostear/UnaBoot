package com.ramostear.unaboot.util;

import java.util.Date;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 0:37.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class TimeAgoUtils {
    private TimeAgoUtils(){}

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = " seconds ago";
    private static final String ONE_SECOND_AGO_ZH = " 秒以前";
    private static final String ONE_MINUTE_AGO = " minutes ago";
    private static final String ONE_MINUTE_AGO_ZH = " 分钟以前";
    private static final String ONE_HOUR_AGO = " hours ago";
    private static final String ONE_HOUR_AGO_ZH = " 小时以前";
    private static final String ONE_DAY_AGO = " days ago";
    private static final String ONE_DAY_AGO_ZH = " 天以前";
    private static final String ONE_MONTH_AGO = " months ago";
    private static final String ONE_MONTH_AGO_ZH = " 月以前";
    private static final String ONE_YEAR_AGO = " years ago";
    private static final String ONE_YEAR_AGO_ZH = " 年以前";
    private static final String ONE_UNKNOWN = " long long ago";
    private static final String ONE_UNKNOWN_ZH = " 很久以前";

    /**
     * default format method and returned english characters.
     * @param date      target date
     * @return          string
     */
    public static String format(Date date){
        return format(FormatType.FORMAT_EN,date);
    }

    /**
     * Format target time as the length of time from current.
     * @param type      english or chinese
     * @param date      target time
     * @return          string
     */
    public static String format(FormatType type,Date date) {
        switch (type){
            case FORMAT_ZH_CN: return format_zh(date);
            default: return format_en(date);
        }
    }

    private static String format_en(Date date) {
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
            return "yesterday";
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


    private static String format_zh(Date date) {
        if (null == date) {
            return ONE_UNKNOWN_ZH;
        }
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO_ZH;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO_ZH;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO_ZH;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO_ZH;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO_ZH;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO_ZH;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    public enum  FormatType {
        FORMAT_EN,FORMAT_ZH_CN;
    }
}
