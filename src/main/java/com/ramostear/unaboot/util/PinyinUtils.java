package com.ramostear.unaboot.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 0:21.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class PinyinUtils {

    private PinyinUtils(){}

    /**
     * Combine the first letter of chinese pinyin into a new string.
     * @param chinese   Chinese
     * @return          string
     */
    public static String toAcronyms(String chinese){
        StringBuilder sb = new StringBuilder();
        char[] chars = chinese.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for(char c:chars){
            if(c > 128){
                try{
                    sb.append(PinyinHelper.toHanyuPinyinStringArray(c, format)[0].charAt(0));
                }catch (BadHanyuPinyinOutputFormatCombination e){
                    e.printStackTrace();
                }
            }else{
                sb.append(c);
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * Convert Chinese characters to Pinyin
     * @param chinese       Chinese characters
     * @return              pinyin string
     */
    public static String toPinyin(String chinese){
        StringBuilder sb = new StringBuilder();
        char[] chars = chinese.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for(char c : chars){
            if(c > 128){
                try {
                    sb.append(PinyinHelper.toHanyuPinyinStringArray(c,format)[0]);
                }catch (BadHanyuPinyinOutputFormatCombination e){
                    e.printStackTrace();
                }
            }else{
                sb.append(c);
            }
        }
        return sb.toString().toLowerCase();
    }
}
