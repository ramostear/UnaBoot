package com.ramostear.unaboot.freemarker.function;

import com.ramostear.unaboot.common.util.EncryptUtils;
import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.domain.valueobject.Gitalk;
import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import com.ramostear.unaboot.service.SettingService;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName GitalkScript
 * @Description 评论插件
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 17:52
 * @Version since UnaBoot-1.0
 **/
@Service
public class GitalkScript extends AbstractMethodModel {

    @Autowired
    private SettingService settingService;

    @Override
    public Object exec(List args) throws TemplateModelException {
        Gitalk gitalk = settingService.gitalk();
        if(!gitalk.isEnabled()){
            return "";
        }else{
            Integer id = getInteger(args,0);
            String dom = getString(args,1);
            StringBuilder sb = new StringBuilder();
            sb.append("var gitalk = new Gitalk({");
            sb.append("clientId:'").append(gitalk.getClientId()).append("',")
              .append("clientSecret:'").append(gitalk.getClientSecret()).append("',")
              .append("repo:'").append(gitalk.getRepo()).append("',")
              .append("owner:'").append(gitalk.getOwner()).append("',")
              .append("admin:'").append(gitalk.getAdmin()).append("',")
              .append("id:'").append(EncryptUtils.MD5("post_"+id)).append("'")
              .append("});")
              .append("gitalk.render('").append(dom).append("');");
            return sb.toString();
        }
    }
}
