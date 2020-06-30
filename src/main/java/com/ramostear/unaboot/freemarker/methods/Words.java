package com.ramostear.unaboot.freemarker.methods;

import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import com.ramostear.unaboot.util.UnaBootUtils;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/29 0029 20:50.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component
public class Words extends AbstractMethodModel {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        String content = getString(arguments,0);
        if(StringUtils.isBlank(content)){
            return 0;
        }
        return UnaBootUtils.getWords(content);
    }
}
