package com.ramostear.unaboot.common.util;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @ClassName ReflectUtils
 * @Description Java反射工具类
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:00
 * @Version since UnaBoot-1.0
 **/
public class ReflectUtils {
    private ReflectUtils(){}

    /**
     * 获取参数类型
     * @param supperType
     * @param types
     * @return
     */
    @Nullable
    public static ParameterizedType getParameterizedType(Class<?>supperType, Type...types){
        Assert.notNull(supperType,"Interface or super type must not be null");
        ParameterizedType type = null;
        for(Type genericType:types){
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            if(parameterizedType.getRawType().getTypeName().equals(supperType.getTypeName())){
                type = parameterizedType;
                break;
            }
        }
        return type;
    }

    @Nullable
    public static ParameterizedType getParameterizedType(@NonNull Class<?> interfaceType, Class<?> implementClz){
        Assert.notNull(interfaceType,"Interface type must not be null");
        Assert.isTrue(interfaceType.isInterface(),"The give type must be a interface");

        if(implementClz == null) return null;

        ParameterizedType type = getParameterizedType(interfaceType,implementClz.getGenericInterfaces());
        if(type != null) return type;

        Class<?> superClass = implementClz.getSuperclass();
        return getParameterizedType(interfaceType,superClass);
    }

    @Nullable
    public static ParameterizedType getParameterizedTypeBySuperClass(@NonNull Class<?>superClassType,Class<?> extensionClass){
        if(extensionClass == null) return null;
        return getParameterizedType(superClassType,extensionClass.getGenericSuperclass());
    }
}
