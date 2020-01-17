package com.ramostear.unaboot.common.util;

import java.util.Random;

/**
 * @ClassName RandomUtils
 * @Description 产生随机字符的工具类
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 19:54
 * @Version since UnaBoot-1.0
 **/
public class RandomUtils {

    private RandomUtils(){}

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
}
