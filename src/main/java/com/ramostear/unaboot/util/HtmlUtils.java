package com.ramostear.unaboot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 0:03.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class HtmlUtils {

    private HtmlUtils(){}

    /**
     * Get the url of all pictures int the html code.
     * @param html      html code
     * @return          pictures url array
     */
    public static List<String> getAllImgUrl(String html){
        List<String> urls = new ArrayList<>();
        Pattern pattern = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher matcher = pattern.matcher(html);
        boolean flag = matcher.find();
        if(flag){
            while (flag){
                String target_img = matcher.group(2);
                Pattern src_pattern = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher src_matcher = src_pattern.matcher(target_img);
                if(src_matcher.find()){
                    String url = src_matcher.group(3);
                    urls.add(url);
                }
                flag = matcher.find();
            }
        }
        return urls;
    }

    /**
     * Add css style to all pictures
     * @param html      html code
     * @param style     css style
     * @return          html code
     */
    public static String addStyleToImg(String html,String style){
        String pattern = "(?i)(\\<img)([^\\>]+\\>)";
        html = html.replaceAll(pattern,"$1 class=\""+style+"\"$2");
        return html;
    }
}
