package com.ramostear.unaboot.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/29 0029 20:33.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Slf4j
public abstract class AbstractTemplateDirectiveModel implements TemplateDirectiveModel {

    protected static final String SINGLE = "result";
    protected static final String MULTI = "results";

    @PostConstruct
    public void initialize() {
        String className = this.getClass().getSimpleName();
        String beanName = StringUtils.uncapitalize(className);
        log.info("UnaBoot Parser: <@unaboot.{}>...</@unaboot.{}>",beanName,beanName);
        UnaBootParser.getInstance().putParser(beanName,this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        try {
            exec(new DirectiveHandler(env,params,loopVars,body));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    abstract public void exec(DirectiveHandler handler) throws Exception;
}
