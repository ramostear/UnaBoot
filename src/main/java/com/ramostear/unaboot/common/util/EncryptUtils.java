package com.ramostear.unaboot.common.util;

import com.ramostear.unaboot.common.UnaBootConst;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

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
        return encrypt(encrypt(salt)+encrypt(input));
    }

    private static String encrypt(String input){
        byte[] code = null;
        try {
            code = MessageDigest.getInstance(UnaBootConst.ALGORITHM_NAME)
                    .digest(input.getBytes());
        }catch (NoSuchAlgorithmException ex){
            code = input.getBytes();
        }
        BigInteger bigInteger = new BigInteger(code);
        return bigInteger.abs().toString(8).toUpperCase();
    }

    public static String simpleHash(String source,String salt){
        return new SimpleHash(
                UnaBootConst.ALGORITHM_NAME,
                source,
                ByteSource.Util.bytes(salt),
                UnaBootConst.HASH_ITERATIONS
        ).toString();
    }

}
