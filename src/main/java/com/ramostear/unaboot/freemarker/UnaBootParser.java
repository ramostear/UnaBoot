package com.ramostear.unaboot.freemarker;

import freemarker.template.*;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/29 0029 20:56.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class UnaBootParser extends SimpleHash {

    private static final long serialVersionUID = -2971523586463346000L;

    private UnaBootParser(ObjectWrapper objectWrapper){
        super(objectWrapper);
    }

    public static UnaBootParser getInstance(){
        return Singleton.INSTANCE.getInstance();
    }

    public void putParser(String key,Object object){
        this.put(key, object);
    }

    private enum Singleton{
        INSTANCE;
        private UnaBootParser singleton;
        Singleton(){
            Version version = Configuration.VERSION_2_3_29;
            DefaultObjectWrapper  defaultObjectWrapper  = new DefaultObjectWrapper(version);
            singleton = new UnaBootParser(defaultObjectWrapper);
        }
        public UnaBootParser getInstance(){
            return singleton;
        }
    }
}
