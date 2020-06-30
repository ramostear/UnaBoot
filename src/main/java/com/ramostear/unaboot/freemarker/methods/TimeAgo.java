package com.ramostear.unaboot.freemarker.methods;

import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import com.ramostear.unaboot.util.TimeAgoUtils;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/29 0029 20:48.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component
public class TimeAgo extends AbstractMethodModel {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Date date = getDate(arguments,0);
        return TimeAgoUtils.format(date);
    }
}
