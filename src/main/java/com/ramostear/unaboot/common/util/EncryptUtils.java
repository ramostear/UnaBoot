package com.ramostear.unaboot.common.util;

import com.ramostear.unaboot.common.UnaBootConst;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName EncryptUtils
 * @Description 数据加密工具类
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 19:46
 * @Version since UnaBoot-1.0
 **/
public class EncryptUtils {

    private EncryptUtils(){}

    /**
     * MD5加密
     * @param input
     * @return
     */
    public static String MD5(String input){
        return MD5(input, UnaBootConst.ENCRYPT_SALT);
    }

    public static String MD5(String input,String salt){
        return encripy(encripy(salt)+encripy(input));
    }

    private static String encripy(String input){
        byte[] code = null;
        try {
            code = MessageDigest.getInstance("md5").digest(input.getBytes());
        }catch (NoSuchAlgorithmException ex){
            code = input.getBytes();
        }
        BigInteger bigInteger = new BigInteger(code);
        return bigInteger.abs().toString(8).toUpperCase();
    }

}
