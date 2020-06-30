package com.ramostear.unaboot.freemarker.methods;

import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/29 0029 20:46.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component
public class NumberFormat extends AbstractMethodModel {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        int number = getInteger(arguments,0);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(number);
    }
}
