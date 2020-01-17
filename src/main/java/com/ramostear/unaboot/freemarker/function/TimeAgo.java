package com.ramostear.unaboot.freemarker.function;

import com.ramostear.unaboot.common.util.TimeAgoUtils;
import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName TimeAgo
 * @Description 计算时间的自定义函数
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 22:07
 * @Version since UnaBoot-1.0
 **/
@Service
public class TimeAgo extends AbstractMethodModel {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Date date = getDate(arguments,0);
        return TimeAgoUtils.format(date);
    }
}
