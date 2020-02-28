package com.ramostear.unaboot.common.util;

import java.awt.*;
import java.util.Random;

/**
 * @ClassName RandomUtils
 * @Description 产生随机字符的工具类
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 19:54
 * @Version since UnaBoot-1.0
 **/
public class RandomUtils {

    private static final char[] CODE_SEQ = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };

    private RandomUtils(){}

    private static Random random = new Random();

    public static String string(int len){
        if(len<=0) len = 8;
        String val = "";
        Random random = new Random();
        for (int i=0;i<len;i++){
            String temp = random.nextInt(2)%2 == 0?"number":"char";
            if(temp.equalsIgnoreCase("char")){
                int nextInt = random.nextInt(2)%2 == 0?65:97;
                val +=(char)(nextInt+random.nextInt(26));
            }else{
                val+= String.valueOf(random.nextInt(10));
            }
        }
        return val.toLowerCase();
    }

    public static Color randomColor(int begin,int end){
        int f = begin;
        int b = end;
        Random random = new Random();
        if(f > 255){
            f=255;
        }
        if(b > 255){
            b = 255;
        }
        return new Color(f+random.nextInt(b-f),f+random.nextInt(b-f),f+random.nextInt(b-f));
    }

    public static String randomStr(int length){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<length;i++){
            sb.append(CODE_SEQ[random.nextInt(CODE_SEQ.length)]);
        }
        return sb.toString();
    }

    public static  int nextInt(int bound){
        return random.nextInt(bound);
    }
}
