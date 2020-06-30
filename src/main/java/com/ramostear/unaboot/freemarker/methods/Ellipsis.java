package com.ramostear.unaboot.freemarker.methods;

import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/29 0029 20:41.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component
public class Ellipsis extends AbstractMethodModel {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        String content = getString(arguments,0);
        int len = getInteger(arguments,1);
        if(StringUtils.isBlank(content)){
            return "";
        }else{
            if(content.length() < len){
                return content;
            }
            content =  content.substring(0,len-3);
            return content+"...";
        }
    }
}
