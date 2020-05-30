package com.ramostear.unaboot.common;

import com.ramostear.unaboot.util.BeanUtils;
import com.ramostear.unaboot.util.ReflectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 4:10.
 * The following is the description information about this file:</p>
 * <p>Object import converter</p>
 */
public interface ImportConvert<T> {

    default T convertTo(){
        ParameterizedType type = getTargetType();
        Objects.requireNonNull(type,"Can not fetch actual type because parameterized type is null");
        Class<T> object = (Class<T>)type.getActualTypeArguments()[0];
        return BeanUtils.transform(this,object);
    }

    default void updateTo(T t){
        BeanUtils.copyProperties(this,t);
    }

    default ParameterizedType getTargetType(){
        return ReflectionUtils.getParameterizedType(ImportConvert.class,this.getClass());
    }
}
