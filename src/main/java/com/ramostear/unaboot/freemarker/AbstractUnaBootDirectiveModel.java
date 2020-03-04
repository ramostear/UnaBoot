package com.ramostear.unaboot.freemarker;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.exception.UnaBootException;
import com.ramostear.unaboot.common.util.UnaBootUtils;
import freemarker.core.Environment;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName AbstractUnaBootDirectiveModel
 * @Description 抽象的自定义指令类
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 21:46
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service
public abstract class AbstractUnaBootDirectiveModel extends ApplicationObjectSupport implements TemplateDirectiveModel {

    @Autowired
    private FreeMarkerConfigurer cfg;

    protected static final String SINGLE = "result";
    protected static final String MULTI = "results";

    @PostConstruct
    public void configuration() throws TemplateModelException{
        String className = this.getClass().getName();
        className = className.substring(className.lastIndexOf(".")+1);
        String beanName = StringUtils.uncapitalize(className);
        String directiveName = UnaBootConst.DIRECTIVE_PREFIX+ UnaBootUtils.camelToUnderline(beanName);
        log.info("UnaBoot custom freemarker value label name: [{}]",directiveName);
        cfg.getConfiguration().setSharedVariable(directiveName,getApplicationContext().getBean(beanName));
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) {
        try {
            execute(new DirectiveHandler(env,params,loopVars,body));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract public  void execute(DirectiveHandler handler) throws Exception;
}
