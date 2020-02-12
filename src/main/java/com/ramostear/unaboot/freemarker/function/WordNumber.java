package com.ramostear.unaboot.freemarker.function;

import com.ramostear.unaboot.common.util.UnaBootUtils;
import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName WordNumber
 * @Description 统计文章字数的方法
 * @Author 树下魅狐
 * @Date 2020/2/12 0012 18:56
 * @Version since UnaBoot-1.0
 **/
@Service
public class WordNumber extends AbstractMethodModel {
    @Override
    public Object exec(List args) throws TemplateModelException {
        String words = getString(args,0);
        if(StringUtils.isBlank(words)){
            return 0;
        }else{
            return UnaBootUtils.getWordCount(words);
        }
    }
}
