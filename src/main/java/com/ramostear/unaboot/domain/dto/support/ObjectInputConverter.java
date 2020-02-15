package com.ramostear.unaboot.domain.dto.support;

import com.ramostear.unaboot.common.util.ReflectUtils;
import com.ramostear.unaboot.common.util.UnaBootBeanUtils;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * @ClassName ObjectInputConverter
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/14 0014 15:53
 * @Version since UnaBoot-1.0
 **/
public interface ObjectInputConverter<T> {

    default T convertTo(){
        ParameterizedType type = type();
        Objects.requireNonNull(type,"Can not fetch actual type because parameterized type is null");
        Class<T> obj = (Class<T>)type.getActualTypeArguments()[0];
        return UnaBootBeanUtils.transform(this,obj);
    }

    default void update(T t){
        UnaBootBeanUtils.update(this,t);
    }

    default ParameterizedType type(){
        return ReflectUtils.getParameterizedType(ObjectInputConverter.class,this.getClass());
    }
}
