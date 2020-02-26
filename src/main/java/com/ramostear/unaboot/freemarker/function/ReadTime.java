package com.ramostear.unaboot.freemarker.function;

import com.ramostear.unaboot.common.util.UnaBootUtils;
import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ReadTime
 * @Description 计算阅读时间，单位分钟，人的正常阅读速度大概是300-500字/分钟
 * @Author 树下魅狐
 * @Date 2020/2/12 0012 18:58
 * @Version since UnaBoot-1.0
 **/
@Service
public class ReadTime extends AbstractMethodModel {
    @Override
    public Object exec(List args) throws TemplateModelException {
        String content = getString(args,0);
        int length = UnaBootUtils.getWordCount(content);
        if(length<400){
            return 0;
        }else{
            return length/400;
        }
    }
}
