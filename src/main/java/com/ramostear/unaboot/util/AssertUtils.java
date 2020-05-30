package com.ramostear.unaboot.util;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/27 0027 23:08.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class AssertUtils {

    private AssertUtils(){}

    /**
     * Mobile phone number regular expression
     */
    private static final String REG_MOBILE = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
    /**
     * Phone number regular expression
     */
    private static final String REG_PHONE = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";

    /**
     * ID Card number regular expression(China)
     */
    private static final String REG_ID = "^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$";

    /**
     * Email address regular expression
     */
    private static final String REG_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * ID verification length
     */
    private static final int ID_VERIFY_LENGTH = 17;
    /**
     * Verify the starting position
     */
    private static final int ID_VERIFY_START_POS = 2;

    private static final String EXT_STR = "x";

    /**
     * Determine target string is mobile phone number.
     * @param number        target string
     * @return              Determine result(boolean)
     */
    public static boolean isMobilePhoneNumber(String number){
        return determine(REG_MOBILE,number);
    }

    /**
     * Determine target string is mobile phone number by custom regular expression.
     * @param regular       custom regular expression
     * @param number        target number
     * @return              determine result(boolean)
     */
    public static boolean isMobilephoneNumber(String regular,String number){
        return determine(regular,number);
    }

    /**
     * Determine whether the target string is a telephone number
     * @param number        target string
     * @return              determine result(boolean)
     */
    public static boolean isTelephoneNumber(String number){
        return determine(REG_PHONE,number);
    }

    /**
     * Determine whether the target string is a telephone number by custom regular expression.
     * @param regular       custom telephone number regular expression
     * @param number        target string
     * @return              boolean
     */
    public static boolean isTelephoneNumber(String regular,String number){
        return determine(regular, number);
    }

    /**
     * determine whether the target string is a email address.
     * @param email     target string
     * @return          boolean
     */
    public static boolean isEmail(String email){
        return determine(REG_EMAIL,email);
    }

    /**
     * determine whether the target string is a email address by custom regular expression.
     * @param regular       custom expression
     * @param email         target string
     * @return              boolean
     */
    public static boolean isEmail(String regular,String email){
        return determine(regular, email);
    }

    /**
     * determine whether the target string is a ID number.
     * @param number        target string
     * @return              boolean
     */
    public static boolean isIDCard(String number){
        if(StringUtils.isEmpty(number)){
            return false;
        }
        Pattern pattern = Pattern.compile(REG_ID);
        Matcher matcher = pattern.matcher(number);
        int[] prefix = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        int[] suffix = new int[] { 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 };
        if(matcher.matches()){
            Map<String,String> cities = initializeCities();
            //Verify area code
            if (cities.get(number.substring(0,2)) == null){
                return false;
            }
            int verifyCode = 0;
            for(int i=0;i<ID_VERIFY_LENGTH;i++){
                verifyCode += Integer.parseInt(number.substring(i,i+1)) * prefix[i];
            }
            int idMod = verifyCode % 11;
            String lastNumber = number.substring(ID_VERIFY_LENGTH);
            if(idMod == ID_VERIFY_START_POS){
                return EXT_STR.equalsIgnoreCase(lastNumber);
            }else{
                return (suffix[idMod] + "").equals(lastNumber);
            }
        }
        return false;
    }

    /**
     * Determine whether it is a number
     * @param reg       number regular expression
     * @param number    target number
     * @return          boolean
     */
    private static boolean determine(String reg,String number){
        return !StringUtils.isEmpty(number) && Pattern.matches(reg,number);
    }

    /**
     * Initialize China`s administrative area code.
     * @return  Map
     */
    private static Map<String,String> initializeCities(){
        Map<String,String> cities = new HashMap<>();
        cities.put("11", "北京");
        cities.put("12", "天津");
        cities.put("13", "河北");
        cities.put("14", "山西");
        cities.put("15", "内蒙古");

        cities.put("21", "辽宁");
        cities.put("22", "吉林");
        cities.put("23", "黑龙江");

        cities.put("31", "上海");
        cities.put("32", "江苏");
        cities.put("33", "浙江");
        cities.put("34", "安徽");
        cities.put("35", "福建");
        cities.put("36", "江西");
        cities.put("37", "山东");

        cities.put("41", "河南");
        cities.put("42", "湖北");
        cities.put("43", "湖南");
        cities.put("44", "广东");
        cities.put("45", "广西");
        cities.put("46", "海南");

        cities.put("50", "重庆");
        cities.put("51", "四川");
        cities.put("52", "贵州");
        cities.put("53", "云南");
        cities.put("54", "西藏");

        cities.put("61", "陕西");
        cities.put("62", "甘肃");
        cities.put("63", "青海");
        cities.put("64", "宁夏");
        cities.put("65", "新疆");
        return cities;
    }
}
