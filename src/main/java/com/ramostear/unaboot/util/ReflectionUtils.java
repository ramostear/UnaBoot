package com.ramostear.unaboot.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 3:57.
 * The following is the description information about this file:</p>
 * <p>Java reflection tool class.</p>
 */
public class ReflectionUtils {

    private ReflectionUtils(){}

    /**
     *Get parameter type.
     * @param clz super class type
     * @param types     Type Array
     * @return ParameterizedType
     */
    @Nullable
    public static ParameterizedType getParameterizedType(Class<?> clz, Type...types){
        Assert.notNull(clz,"Interface or super type must not be null");
        ParameterizedType type = null;
        for(Type genericType : types){
            ParameterizedType parameterizedType = (ParameterizedType)genericType;
            if(parameterizedType.getRawType().getTypeName().equals(clz.getTypeName())){
                type = parameterizedType;
                break;
            }
        }
        return type;
    }

    @Nullable
    public static ParameterizedType getParameterizedType(@NonNull Class<?> clz, Class<?> implClz){
        Assert.notNull(clz,"Interface type must not be null.");
        Assert.isTrue(clz.isInterface(),"The target type must be a interface.");
        if(implClz == null) return null;
        ParameterizedType type = getParameterizedType(clz,implClz.getGenericInterfaces());
        if(type != null) return type;
        Class<?> superClass = implClz.getSuperclass();
        return getParameterizedType(implClz,superClass);
    }

    @NonNull
    public static ParameterizedType getParameterizedTypeBySuperClass(@NonNull Class<?> superClass,Class<?> subClass){
        if(subClass == null) return null;
        return getParameterizedType(superClass,subClass.getGenericSuperclass());
    }
}
