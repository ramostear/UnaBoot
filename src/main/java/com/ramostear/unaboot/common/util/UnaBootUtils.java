package com.ramostear.unaboot.common.util;

import com.ramostear.unaboot.common.UnaBootConst;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @ClassName UnaBootUtils
 * @Description UnaBoot系统工具类
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:45
 * @Version since UnaBoot-1.0
 **/
public class UnaBootUtils {

    /**
     * 判断系统是否安装
     * @return
     */
    public static Boolean isInstall(){
        String flagFile = UnaBootUtils.class.getResource("/")
                .getPath()+ UnaBootConst.SEPARATOR+UnaBootConst.INSTALL_FLAG_FILE;
        File file = new File(flagFile);
        if(file.exists()){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }

    /**
     * 驼峰命名转换成下划线分割
     * @param camelName
     * @return
     */
    public static String camelToUnderline(String camelName){
        camelName = StringUtils.uncapitalize(camelName);
        char[] letters = camelName.toCharArray();
        StringBuffer buffer = new StringBuffer();
        for(char letter:letters){
            if(Character.isUpperCase(letter)){
                buffer.append("_"+letter);
            }else{
                buffer.append(letter);
            }
        }
        return StringUtils.lowerCase(buffer.toString());
    }
}
