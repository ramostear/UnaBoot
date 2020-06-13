package com.ramostear.unaboot.config.support;

import com.ramostear.sft.ShiroFreemarkerTags;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/30 0030 11:19.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class CustomFreemarkerConfigurer extends FreeMarkerConfigurer {

    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        Configuration configuration = getConfiguration();
        Version version = Configuration.VERSION_2_3_29;
        DefaultObjectWrapper  defaultObjectWrapper  = new DefaultObjectWrapper(version);
        configuration.setSharedVariable("shiro",new ShiroFreemarkerTags(defaultObjectWrapper));
    }
}
