package com.ramostear.unaboot.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @ClassName CronUtils
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 17:47
 * @Version since UnaBoot-1.0
 **/
public class CronUtils {

    private CronUtils(){}

    private static final SimpleDateFormat format = new SimpleDateFormat("ss mm HH dd MM ? yyyy");

    public static String getCron(Date date){
        String timeExpression = "0/1 * * * * ? *";
        if(Objects.nonNull(date)){
            timeExpression = format.format(date);
        }
        return timeExpression;
    }

    public static Date parse(String dateCharacter){
        try {
            return format.parse(dateCharacter);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
