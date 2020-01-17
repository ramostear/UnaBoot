package com.ramostear.unaboot.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @ClassName PropertyUtils
 * @Description 读写.properties文件的工具类
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:54
 * @Version since UnaBoot-1.0
 **/
@Slf4j
public class PropertyUtils {

    private PropertyUtils(){}

    /**
     * 根据properties文件名读取配置内容
     * @param filename
     * @return
     */
    @NotNull
    public static Properties read(@NotNull String filename){
        String path = getFilePath(filename);
        Properties properties = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(path);
            properties.load(input);
        }catch (IOException ex){
            log.error(ex.getMessage());
        }
        return properties;
    }

    /**
     * 将配置信息写入配置文件
     * @param filename      配置文件名
     * @param keyValue      配置信息
     * @return
     */
    public static boolean write(@NotNull String filename, @NotNull Map<String,String>keyValue){
        String path = getFilePath(filename);
        try {
            if(!keyValue.isEmpty()){
                Properties properties = new Properties();
                InputStream input = new BufferedInputStream(new FileInputStream(path));
                properties.load(input);
                Set<String> keys = keyValue.keySet();
                keys.forEach(key->{
                    String value = keyValue.get(key);
                    properties.put(key,value);
                });
                FileOutputStream output = new FileOutputStream(path);
                properties.store(output,"update properties");
                output.flush();
                output.close();
                input.close();
                return true;
            }else{
                return false;
            }

        }catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取配置文件路径
     * @param filename
     * @return
     */
    private static String getFilePath(String filename){
        return PropertyUtils.class.getClassLoader().getResource(filename).getPath();
    }
}
