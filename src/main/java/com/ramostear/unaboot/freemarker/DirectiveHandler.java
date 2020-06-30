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

import static com.ramostear.unaboot.util.FreemarkerUtils.*;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/29 0029 20:37.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
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
        return toStr(getModel(name));
    }

    public Integer getInteger(String name) throws TemplateModelException {
        return toInteger(getModel(name));
    }

    public Short getShort(String name) throws TemplateModelException {
        return toShort(getModel(name));
    }

    public Long getLong(String name) throws TemplateModelException {
        return toLong(getModel(name));
    }

    public Double getDouble(String name) throws TemplateModelException {
        return toDouble(getModel(name));
    }

    public String[] getStringArray(String name) throws TemplateModelException {
        return toArray(getModel(name));
    }

    public Boolean getBoolean(String name) throws TemplateModelException {
        return toBoolean(getModel(name));
    }

    public Date getDate(String name) throws TemplateModelException {
        return toDate(getModel(name));
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
            ret =  toStr(getEnvModel("base"));
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
