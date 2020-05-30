package com.ramostear.unaboot.util;

import com.ramostear.unaboot.exception.BeanException;
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
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 4:12.
 * The following is the description information about this file:</p>
 * <p>Custom Bean Tool</p>
 */
public class BeanUtils {
    private BeanUtils(){}

    /**
     * Conversion between two classes.
     * @param source    source object
     * @param target    target class
     * @param <T>       target type
     * @return          target instance
     */
    public static <T> T transform(@Nullable Object source, Class<T> target){
        Assert.notNull(target,"Target class must not be null.");
        if(source == null) return null;
        try {
            T targetInstance = target.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source,targetInstance,getIgnoreProperties(source));
            return targetInstance;
        }catch (Exception e){
            throw new BeanException("Failed to new "+ target.getName()+"instance or copy properties",e);
        }
    }

    /**
     * Convert multiple source objects to target class object instances.
     * @param sources       sources object collection
     * @param target        target class
     * @param <T>           target type
     * @return              target object instances
     */
    public static <T> List<T> transform(Collection<?> sources,Class<T> target){
        if(CollectionUtils.isEmpty(sources)) return Collections.emptyList();
        return sources.stream()
                .map(source -> transform(source,target))
                .collect(Collectors.toList());
    }

    /**
     * Use the value of the source object to overwrite the value of the target object properties.
     * @param source    source object
     * @param target    target object
     * @return          target object data
     */
    public static Object copyProperties(@NonNull Object source, @NonNull Object target){
        Assert.notNull(source,"Source object must not be null.");
        Assert.notNull(target,"Target object must not be null.");
        try{
            org.springframework.beans.BeanUtils.copyProperties(source,target,getIgnoreProperties(source));
        }catch (BeansException e){
            throw new BeanException("Failed to copy properties from source object :" +source.getClass().getName()+" to target object : "+ target.getClass().getName()+" .",e);
        }
        return target;
    }

    /**
     * Get target object ignore attributes array.
     * @param obj   target object
     * @return      ignore attributes array
     */
    private static String[] getIgnoreProperties(Object obj){
        return getIgnorePropertyArray(obj).toArray(new String[0]);
    }

    /**
     * When the object attribute value is empty,it will be
     * automatically added to ignore list.
     * @param obj   target object
     * @return  ignore list
     */
    private static Set<String> getIgnorePropertyArray(Object obj){
        Assert.notNull(obj,"source object must not be null");
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(obj);
        PropertyDescriptor[] descriptors = beanWrapper.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for(PropertyDescriptor descriptor : descriptors){
            String propertyName = descriptor.getName();
            Object propertyValue = beanWrapper.getPropertyValue(propertyName);
            if(propertyValue == null){
                emptyNames.add(propertyName);
            }
        }
        return emptyNames;
    }
}
