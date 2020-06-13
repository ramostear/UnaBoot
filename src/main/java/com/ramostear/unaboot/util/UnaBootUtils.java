package com.ramostear.unaboot.util;

import com.ramostear.unaboot.common.Constants;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 3:19.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class UnaBootUtils {
    private UnaBootUtils(){}

    /**
     * Determine whether the system is successfully installed.
     * @return  result
     */
    public static boolean isInstalled(){
        String path = UnaBootUtils.class.getResource("/")
                .getPath()+ Constants.SEPARATOR+Constants.MARK_FILE;
        File file = new File(path);
        return file.exists();
    }

    public static void marked() throws IOException {
        String markFile = UnaBootUtils.class.getResource("/")
                .getPath()+Constants.SEPARATOR+Constants.MARK_FILE;
        File file = new File(markFile);
        if(!file.exists()){
            file.createNewFile();
        }
    }

    /**
     * Load the properties file under the classpath according to the file name.
     * @param fileName          filename
     * @return                  properties file
     */
    public static Properties loadPropertyFile(String fileName){
        String path = getFilePath(fileName);
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(path);
            prop.load(input);
            return prop;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            return null;
        }finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Set properties file value according to key.
     * @param filename  file name
     * @param kv        key-value collection
     * @return          true/false
     */
    public static boolean setPropertyFile(String filename, Map<String, String> kv) {
        String filePath = getFilePath(filename);
        if(CollectionUtils.isEmpty(kv)){
            return false;
        }else{
            try {
                Properties prop = new Properties();
                InputStream input = new BufferedInputStream(new FileInputStream(filePath));
                prop.load(input);
                Set<String> keys = kv.keySet();
                keys.forEach(key-> prop.put(key,kv.get(key)));
                FileOutputStream output = new FileOutputStream(filePath);
                prop.store(output,"update properties");
                output.flush();
                output.close();
                input.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }


    public static String MD5(String input){
        return MD5(input,Constants.ENCRYPT_SALT);
    }
    public static String MD5(String input,String salt){
        return encrypt(encrypt(salt)+encrypt(input));
    }

    public static String simpleHash(String source,String salt){
        return new SimpleHash(Constants.ALGORITHM_NAME,source, ByteSource.Util.bytes(salt),Constants.HASH_ITERATIONS).toString();
    }

    /**
     * Convert date to cron expression
     * @param date          date
     * @return              cron expression
     */
    public static String toCronExp(Date date){
        String defaultExp = "0/1 * * * * ? *";
        if(Objects.nonNull(date)){
            defaultExp = Constants.CRON_DATE_FORMAT.format(date);
        }
        return defaultExp;
    }

    /**
     * Convert Cron expression to Date
     * @param expression        cron expression
     * @return                  Date
     */
    public static Date cronExpToDate(String expression){
        try {
            return Constants.CRON_DATE_FORMAT.parse(expression);
        } catch (ParseException e) {
            return DateTimeUtils.now();
        }
    }

    public static String fileSize(long size){
        if(size < 1024){
            return size+"Bytes";
        }else if (size >= 1024 && size < 1024*1024){
            return (int)size/1024+"KB";
        }else if(size >= 1024* 1024 && size < 1024*1024*1024){
            return (int)size/(1024*1024)+"MB";
        }else{
            return size/(1024*1024*1024)+"TB";
        }
    }

    public static String random(int len){
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

    /**
     * Obtain the file path under the classpath according to the file name.
     * @param name      file name
     * @return          file path
     */
    private static String getFilePath(String name){
        return UnaBootUtils.class.getClassLoader().getResource(name).getPath();
    }


    private static String encrypt(String input){
        byte[] code = null;
        try {
            code = MessageDigest.getInstance(Constants.ALGORITHM_NAME).digest(input.getBytes());
        } catch (NoSuchAlgorithmException e) {
            code = input.getBytes();
        }
        BigInteger bigInteger = new BigInteger(code);
        return bigInteger.abs().toString(32).toUpperCase();
    }
}
