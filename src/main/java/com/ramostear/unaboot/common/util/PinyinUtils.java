package com.ramostear.unaboot.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @ClassName PinyinUtils
 * @Description 汉语拼音工具类
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:20
 * @Version since UnaBoot-1.0
 **/
public class PinyinUtils {

    private PinyinUtils(){}

    /**
     * 将汉字的拼音首字母组合成字符串
     * @param chinese
     * @return
     */
    public static String chineseToPinyinByFirstChar(String chinese){
        String pinyin = "";
        char[] chars = chinese.toCharArray();
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for(char c:chars){
            if(c > 128){
                try {
                    pinyin += PinyinHelper.toHanyuPinyinStringArray(c,outputFormat)[0].charAt(0);
                }catch (BadHanyuPinyinOutputFormatCombination ex){
                    ex.printStackTrace();
                }
            }else{
                pinyin += c;
            }
        }
        return pinyin.toLowerCase();
    }

    /**
     * 汉字转为拼音字符串
     * @param chinese
     * @return
     */
    public static String chineseToPinyin(String chinese){
        String pinyin = "";
        char[] chars = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for(char c : chars){
            if(c > 128){
                try {
                    pinyin += PinyinHelper.toHanyuPinyinStringArray(c,defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination ex) {
                    ex.printStackTrace();
                }
            }else{
                pinyin += c;
            }
        }
        return pinyin.toLowerCase();
    }
}
