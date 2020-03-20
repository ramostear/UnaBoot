package com.ramostear.unaboot.common.util;

import com.ramostear.unaboot.common.UnaBootConst;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName HTMLUtils
 * @Description 操作HTML文档工具类
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:06
 * @Version since UnaBoot-1.0
 **/
public class HTMLUtils {

    private HTMLUtils(){}

    /**
     * 获取html文档中所有img标签的图片地址
     * @param html
     * @return
     */
    public static List<String> matchImgAddr(String html){
        List<String> addressList = new ArrayList<>();
        Pattern imgPattern = Pattern.compile(UnaBootConst.IMG_SRC_PATTERN);
        Matcher imgMatcher = imgPattern.matcher(html);
        boolean isFound = imgMatcher.find();
        if(isFound){
            while (isFound){
                String imgContent = imgMatcher.group(2);
                Pattern srcPattern = Pattern.compile(UnaBootConst.IMG_SRC_VAL_PATTERN);
                Matcher srcMatcher = srcPattern.matcher(imgContent);
                if(srcMatcher.find()){
                    String address = srcMatcher.group(3);
                    addressList.add(address);
                }
                isFound = imgMatcher.find();
            }
        }
        return addressList;
    }

    /**
     * 给html中的img标签添加样式表
     * @param html
     * @param styleSheet
     * @return
     */
    public static String addStyleSheetToImg(String html,String styleSheet){
        return html.replace(UnaBootConst.IMG_PATTERN,"$1 class=\""+styleSheet+"\"$2");
    }
}
