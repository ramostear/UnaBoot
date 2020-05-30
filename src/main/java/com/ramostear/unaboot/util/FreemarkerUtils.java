package com.ramostear.unaboot.util;

import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 15:20.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class FreemarkerUtils {
    private FreemarkerUtils(){}

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int DATE_FORMAT_LENGTH = 19;
    private static final DateFormat DATE_FORMAT_SHORT = new SimpleDateFormat("yyyy-MM-dd");
    private static final int DATE_FORMAT_SHORT_LENGTH = 10;

    /**
     * Convert freemarker model to string
     * @param model         freemarker model
     * @return              string
     * @throws TemplateModelException       TemplateModelException
     */
    public static String toString(TemplateModel model) throws TemplateModelException{
        if(null != model){
            if(model instanceof TemplateScalarModel){
                return ((TemplateScalarModel)model).getAsString();
            }else if (model instanceof TemplateNumberModel){
                return ((TemplateNumberModel)model).getAsNumber().toString();
            }
        }
        return null;
    }

    /**
     * Convert freemarker model to string array.
     * @param model             freemarker model
     * @return                  array
     * @throws TemplateModelException       TemplateModelException
     */
    public static String[] toArray(TemplateModel model) throws TemplateModelException {
        if(null != model){
            if(model instanceof TemplateSequenceModel){
                TemplateSequenceModel sequenceModel = (TemplateSequenceModel)model;
                String[] values = new String[sequenceModel.size()];
                for(int i=0;i<sequenceModel.size();i++){
                    values[i] = toString(sequenceModel.get(i));
                }
                return values;
            }else{
                String str = toString(model);
                if(StringUtils.isNotBlank(str)){
                    return StringUtils.split(str,",");
                }
            }
        }
        return null;
    }

    /**
     * Convert freemarker model to hash model
     * @param model     freemarker model
     * @return          hash model
     */
    public static TemplateHashModel toHashModel(TemplateModel model){
        if(null != model){
            if(model instanceof TemplateHashModelEx){
                return (TemplateHashModelEx) model;
            }else if(model instanceof TemplateHashModel){
                return (TemplateHashModel) model;
            }
        }
        return null;
    }

    /**
     * Convert freemarker model to integer
     * @param model     freemarker model
     * @return          Integer object
     * @throws TemplateModelException       TemplateModelException
     */
    public static Integer toInteger(TemplateModel model) throws TemplateModelException {
        if(null != model){
            if(model instanceof TemplateNumberModel){
                return ((TemplateNumberModel)model).getAsNumber().intValue();
            }else if(model instanceof TemplateScalarModel){
                String str = ((TemplateScalarModel)model).getAsString();
                if(StringUtils.isNotBlank(str)){
                    return Integer.parseInt(str);
                }
            }
        }
        return null;
    }

    /**
     * Convert freemarker model to short.
     * @param model             freemarker model
     * @return                  Short object
     * @throws TemplateModelException       TemplateModelException
     */
    public static Short toShort(TemplateModel model) throws TemplateModelException {
        if(null != model){
            if(model instanceof TemplateNumberModel){
                return ((TemplateNumberModel)model).getAsNumber().shortValue();
            }else if(model instanceof TemplateScalarModel){
                String str = ((TemplateScalarModel)model).getAsString();
                if(StringUtils.isNotBlank(str)){
                    return Short.parseShort(str);
                }
            }
        }
        return null;
    }

    /**
     * Convert freemarker model to Long
     * @param model             freemarker model
     * @return                  Long object
     * @throws TemplateModelException       TemplateModelException
     */
    public static Long toLong(TemplateModel model) throws TemplateModelException {
        if(null != model){
            if(model instanceof TemplateNumberModel){
                return ((TemplateNumberModel)model).getAsNumber().longValue();
            }else if(model instanceof TemplateScalarModel){
                String str = ((TemplateScalarModel)model).getAsString();
                if(StringUtils.isNotBlank(str)){
                    return Long.parseLong(str);
                }
            }
        }
        return null;
    }

    /**
     * Convert freemarker model to Double
     * @param model             freemarker model
     * @return                  Double object
     * @throws TemplateModelException           TemplateModelException
     */
    public static Double toDouble(TemplateModel model) throws TemplateModelException {
        if(null != model){
            if(model instanceof TemplateNumberModel){
                return ((TemplateNumberModel)model).getAsNumber().doubleValue();
            }else if(model instanceof  TemplateScalarModel){
                String str = ((TemplateScalarModel)model).getAsString();
                if(StringUtils.isNotBlank(str)){
                    return Double.parseDouble(str);
                }
            }
        }
        return null;
    }

    /**
     * Convert freemarker model to boolean
     * @param model             freemarker model
     * @return                  Boolean object
     * @throws TemplateModelException           TemplateModelException
     */
    public static Boolean toBoolean(TemplateModel model) throws TemplateModelException {
        if(null != model){
            if(model instanceof TemplateBooleanModel){
                return ((TemplateBooleanModel)model).getAsBoolean();
            }else if(model instanceof TemplateNumberModel){
                return !(((TemplateNumberModel)model).getAsNumber().intValue() == 0);
            }else if(model instanceof TemplateScalarModel){
                String str = ((TemplateScalarModel)model).getAsString();
                if(StringUtils.isNotBlank(str)){
                    return Boolean.valueOf(str);
                }
            }
        }
        return false;
    }

    /**
     * Convert freemarker model to date
     * @param model             freemarker model
     * @return                  date
     * @throws TemplateModelException   TemplateModelException
     */
    public static Date toDate(TemplateModel model) throws TemplateModelException {
        if(null != model){
            if(model instanceof TemplateDateModel){
                return ((TemplateDateModel)model).getAsDate();
            }else if(model instanceof TemplateScalarModel){
                String str = StringUtils.trimToEmpty(((TemplateScalarModel)model).getAsString());
                return dateFormat(str);
            }
        }
        return null;
    }

    /**
     * Convert string to date.
     * @param str       date string
     * @return          date
     */
    private static Date dateFormat(String str){
        Date date = null;
        try {
            if(str.length() == DATE_FORMAT_LENGTH){
                date = DATE_FORMAT.parse(str);
            }else if(str.length() == DATE_FORMAT_SHORT_LENGTH){
                date = DATE_FORMAT_SHORT.parse(str);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
