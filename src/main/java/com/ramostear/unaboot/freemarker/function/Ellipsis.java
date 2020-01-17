package com.ramostear.unaboot.freemarker.function;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName Ellipsis
 * @Description 超出部分省略自定义函数
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 22:18
 * @Version since UnaBoot-1.0
 **/
@Service
public class Ellipsis extends AbstractMethodModel {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        String content = getString(arguments,0);
        int length = getInteger(arguments,1);
        if(content.length() < length){
            return content;
        }
        content = content.substring(0,length-3);
        return content+ UnaBootConst.ELLIPSIS_VALUE;
    }
}
