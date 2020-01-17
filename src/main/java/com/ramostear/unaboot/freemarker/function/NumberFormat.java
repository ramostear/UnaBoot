package com.ramostear.unaboot.freemarker.function;

import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @ClassName NumberFormat
 * @Description 格式化数字的自定义函数
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 22:13
 * @Version since UnaBoot-1.0
 **/
@Service
public class NumberFormat extends AbstractMethodModel {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        int number = getInteger(arguments,0);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(number);
    }
}
