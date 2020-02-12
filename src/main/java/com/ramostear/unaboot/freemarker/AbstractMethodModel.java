package com.ramostear.unaboot.freemarker;

import com.ramostear.unaboot.common.UnaBootConst;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

import static com.ramostear.unaboot.common.util.FreemarkerModelUtils.*;

/**
 * @ClassName AbstractMethodModel
 * @Description 抽象的自定义FreeMarker函数
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 21:31
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service
public abstract class AbstractMethodModel extends ApplicationObjectSupport implements TemplateMethodModelEx {

    @Autowired
    private FreeMarkerConfigurer cfg;

    @PostConstruct
    public void configuration() throws TemplateModelException{
        String className = this.getClass().getName();
        className = className.substring(className.lastIndexOf(".")+1);
        String beanName = StringUtils.uncapitalize(className);
        String methodName = UnaBootConst.DIRECTIVE_PREFIX+StringUtils.uncapitalize(className);
        log.info("UnaBoot custom freemarker method label name:[{}]",methodName);
        cfg.getConfiguration().setSharedVariable(methodName,getApplicationContext().getBean(beanName));
    }


    public TemplateModel getModel(List<TemplateModel>arguments,int index){
        if(index < arguments.size()){
            return arguments.get(index);
        }
        return null;
    }


    public String getString(List<TemplateModel> arguments,int index) throws TemplateModelException{
        return convertToString(getModel(arguments,index));
    }

    public Integer getInteger(List<TemplateModel> arguments,int index) throws TemplateModelException{
        return convertToInteger(getModel(arguments, index));
    }

    public Long getLong(List<TemplateModel> arguments,int index) throws TemplateModelException{
        return convertToLong(getModel(arguments,index));
    }

    public Date getDate(List<TemplateModel> arguments, int index) throws TemplateModelException{
        return convertToDate(getModel(arguments,index));
    }

    public Boolean getBoolean(List<TemplateModel> arguments,int index) throws TemplateModelException{
        return convertToBoolean(getModel(arguments,index));
    }


    public Double getDouble(List<TemplateModel>arguments,int index) throws TemplateModelException{
        return convertToDouble(getModel(arguments,index));
    }

    public TemplateHashModel getMap(List<TemplateModel>arguments, int index) throws TemplateModelException{
        return convertToMap(getModel(arguments,index));
    }
}
