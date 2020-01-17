package com.ramostear.unaboot.common.util;

import com.ramostear.unaboot.common.exception.UnaBootBeanUtilsException;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName UnaBootBeanUtils
 * @Description 自定义Bean工具类
 * @Author 树下魅狐
 * @Date 2020/1/16 0016 20:28
 * @Version since UnaBoot-1.0
 **/
public class UnaBootBeanUtils {

    private UnaBootBeanUtils(){}

    /**
     * Transform Bean values from source Object to target Class.
     * @param source            source data
     * @param targetClz         target class
     * @param <T>               target class type
     * @return                  instance with specified type copying from source data.
     */
    public static <T> T transform(@Nullable Object source, @NonNull Class<T> targetClz){
        Assert.notNull(targetClz,"Target class must not be null.");
        if(source == null){
            return null;
        }
        try {
            T targetInstance = targetClz.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source,targetInstance,getNullPropNames(source));
            return targetInstance;
        } catch (Exception e) {
            throw new UnaBootBeanUtilsException("Failed to new " + targetClz.getName() + " instance or copy properties",e);
        }
    }

    @NonNull
    public static <T> List<T> transform(Collection<?> sources, @NonNull Class<T> targetClz){
        if(CollectionUtils.isEmpty(sources))
            return Collections.emptyList();
        return sources.stream()
                .map(source -> transform(source,targetClz))
                .collect(Collectors.toList());
    }

    /**
     * update properties from source to target
     * @param source        source data
     * @param target        target data
     */
    public static void update(@NonNull Object source,@NonNull Object target){
        Assert.notNull(source,"source object must not be null.");
        Assert.notNull(target,"target object must not be null.");
        try {
            org.springframework.beans.BeanUtils.copyProperties(source,target,getNullPropNames(source));
        }catch (BeansException ex){
            throw new UnaBootBeanUtilsException("Failed to copy properties",ex);
        }
    }


    /**
     * get the property names with a null value.
     * @param source        source data
     * @return              null name array of property.
     */
    @NonNull
    private static String[] getNullPropNames(Object source){
        return getNullPropNameSet(source).toArray(new String[0]);
    }

    @NonNull
    private static Set<String> getNullPropNameSet(@NonNull Object source){
        Assert.notNull(source,"source object must not be null.");
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for(PropertyDescriptor descriptor : propertyDescriptors){
            String propName = descriptor.getName();
            Object propValue = beanWrapper.getPropertyValue(propName);
            if(propValue == null){
                emptyNames.add(propName);
            }
        }
        return emptyNames;
    }
}
