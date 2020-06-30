package com.ramostear.unaboot.freemarker;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

import static com.ramostear.unaboot.util.FreemarkerUtils.*;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/29 0029 20:24.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Slf4j
public abstract class AbstractMethodModel implements TemplateMethodModelEx {

    @PostConstruct
    public void initialize() {
        String className = this.getClass().getSimpleName();
        String beanName = StringUtils.uncapitalize(className);
        log.info("UnaBoot Parser Method Name:${unaboot.{}()}",beanName);
        UnaBootParser.getInstance().putParser(beanName,this);
    }

    public TemplateModel getModel(List<TemplateModel> arguments, int index){
        if(index < arguments.size()){
            return arguments.get(index);
        }
        return null;
    }


    public String getString(List<TemplateModel> arguments,int index) throws TemplateModelException {
        return toStr(getModel(arguments,index));
    }

    public Integer getInteger(List<TemplateModel> arguments,int index) throws TemplateModelException{
        return toInteger(getModel(arguments, index));
    }

    public Long getLong(List<TemplateModel> arguments,int index) throws TemplateModelException{
        return toLong(getModel(arguments,index));
    }

    public Date getDate(List<TemplateModel> arguments, int index) throws TemplateModelException{
        return toDate(getModel(arguments,index));
    }

    public Boolean getBoolean(List<TemplateModel> arguments,int index) throws TemplateModelException{
        return toBoolean(getModel(arguments,index));
    }


    public Double getDouble(List<TemplateModel>arguments,int index) throws TemplateModelException{
        return toDouble(getModel(arguments,index));
    }

    public TemplateHashModel getMap(List<TemplateModel>arguments, int index) throws TemplateModelException{
        return toHashModel(getModel(arguments,index));
    }
}
