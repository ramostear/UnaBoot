package com.ramostear.unaboot.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import static com.ramostear.unaboot.common.util.FreemarkerModelUtils.*;

/**
 * @ClassName DirectiveHandler
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 21:44
 * @Version since UnaBoot-1.0
 **/
public class DirectiveHandler {

    private Environment env;

    private Map<String, TemplateModel> params;

    private TemplateModel[] loopVars;

    private TemplateDirectiveBody body;

    private Environment.Namespace namespace;

    public DirectiveHandler(Environment env,
                            Map<String,TemplateModel> params,
                            TemplateModel[] loopVars,
                            TemplateDirectiveBody body){
        this.env = env;
        this.loopVars = loopVars;
        this.params = params;
        this.body = body;
        this.namespace = env.getCurrentNamespace();
    }

    public void render() throws IOException, TemplateException {
        Assert.notNull(body, "must have template directive body");
        body.render(env.getOut());
    }

    public void renderString(String text) throws Exception {
        StringWriter writer = new StringWriter();
        writer.append(text);
        env.getOut().write(text);
    }

    public DirectiveHandler put(String key, Object value) throws TemplateModelException {
        namespace.put(key, wrap(value));
        return this;
    }

    public String getString(String name) throws TemplateModelException {
        return convertToString(getModel(name));
    }

    public Integer getInteger(String name) throws TemplateModelException {
        return convertToInteger(getModel(name));
    }

    public Short getShort(String name) throws TemplateModelException {
        return convertToShort(getModel(name));
    }

    public Long getLong(String name) throws TemplateModelException {
        return convertToLong(getModel(name));
    }

    public Double getDouble(String name) throws TemplateModelException {
        return convertToDouble(getModel(name));
    }

    public String[] getStringArray(String name) throws TemplateModelException {
        return convertToStringArray(getModel(name));
    }

    public Boolean getBoolean(String name) throws TemplateModelException {
        return convertToBoolean(getModel(name));
    }

    public Date getDate(String name) throws TemplateModelException {
        return convertToDate(getModel(name));
    }

    public String getString(String name, String defaultValue) throws Exception {
        String result = getString(name);
        return null == result ? defaultValue : result;
    }

    public Integer getInteger(String name, int defaultValue) throws Exception {
        Integer result = getInteger(name);
        return null == result ? defaultValue : result;
    }

    public Long getLong(String name, long defaultValue) throws Exception {
        Long result = getLong(name);
        return null == result ? defaultValue : result;
    }


    public String getContextPath() {
        String ret = null;
        try {
            ret =  convertToString(getEnvModel("base"));
        } catch (TemplateModelException e) {
        }
        return ret;
    }

    /**
     * 包装对象
     * @param object
     * @return
     * @throws TemplateModelException
     */
    public TemplateModel wrap(Object object) throws TemplateModelException {
        return env.getObjectWrapper().wrap(object);
    }

    /**
     * 获取局部变量
     * @param name
     * @return
     * @throws TemplateModelException
     */
    public TemplateModel getEnvModel(String name) throws TemplateModelException {
        return env.getVariable(name);
    }

    public void write(String text) throws IOException {
        env.getOut().write(text);
    }

    private TemplateModel getModel(String name) {
        return params.get(name);
    }
}
