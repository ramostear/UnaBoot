package com.ramostear.unaboot.freemarker.function;

import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName GitalkSource
 * @Description Gitalk插件资源地址
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 18:03
 * @Version since UnaBoot-1.0
 **/
@Service
public class GitalkSource extends AbstractMethodModel {
    @Override
    public Object exec(List args) throws TemplateModelException {
        String type = getString(args,0);
        if("css".equalsIgnoreCase(type)){
            return "https://cdn.bootcss.com/gitalk/1.5.0/gitalk.css";
        }else if("js".equalsIgnoreCase(type)){
            return "https://cdn.bootcss.com/gitalk/1.5.0/gitalk.js";
        }else{
           return "";
        }
    }
}
