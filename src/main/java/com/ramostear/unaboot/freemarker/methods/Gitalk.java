package com.ramostear.unaboot.freemarker.methods;

import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import com.ramostear.unaboot.service.SettingService;
import com.ramostear.unaboot.util.UnaBootUtils;
import freemarker.template.TemplateModelException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 17:25.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component
public class Gitalk extends AbstractMethodModel {

    private final SettingService settingService;

    Gitalk(SettingService settingService){
        this.settingService = settingService;
    }

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        int type = getInteger(arguments,0);
        type = Math.max(type,0);
        Type item = Type.obtain(type);
        if(item == Type.CSS){
            return "https://cdn.bootcss.com/gitalk/1.5.0/gitalk.css";
        }else if (item == Type.JS){
            return "https://cdn.bootcss.com/gitalk/1.5.0/gitalk.js";
        }else if(item == Type.DEFAULT){
            return initializeFunction(arguments);
        }else{
            return "";
        }
    }

    private String initializeFunction(List arguments) throws TemplateModelException {
        com.ramostear.unaboot.domain.vo.Gitalk gitalk = settingService.gitalk();
        if(gitalk.isEnabled()){
            int id = getInteger(arguments,1);
            String dom = getString(arguments,2);
            return "<script type='text/javascript'>" +
                    "var gitalk = new Gitalk({" +
                    "clientID:'" + gitalk.getClientId() + "'," +
                    "clientSecret:'" + gitalk.getClientSecret() + "'," +
                    "repo:'" + gitalk.getRepo() + "'," +
                    "owner:'" + gitalk.getOwner() + "'," +
                    "admin:'" + gitalk.getAdmin() + "'," +
                    "id:'" + UnaBootUtils.MD5("post_" + id) + "'" +
                    "});" +
                    "gitalk.render('" + dom + "');" +
                    "</script>";
        }else {
            return "";
        }
    }


    enum Type{
        DEFAULT(0),
        CSS(1),
        JS(2);
        private int code;
        Type(int code){
            this.code = code;
        }

        public static Type obtain(int code){
            Type[] types = Type.values();
            Type type = Type.DEFAULT;
            for(Type item:types){
                if(item.code == code){
                    type = item;
                    break;
                }
            }
            return type;
        }


    }
}
