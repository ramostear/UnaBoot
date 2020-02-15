package com.ramostear.unaboot.common.util;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName ServiceUtils
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/14 0014 16:21
 * @Version since UnaBoot-1.0
 **/
public class ServiceUtils {
    private ServiceUtils(){}

    @NonNull
    public static <ID,T> Set<ID> fetchProperty(final Collection<T> datas, Function<T,ID> function){
        return CollectionUtils.isEmpty(datas)?
                Collections.emptySet():
                datas.stream().map(function).collect(Collectors.toSet());
    }

    @NonNull
    public static <ID,D> Map<ID, List<D>> convertTo(Collection<ID> ids, Collection<D> list, Function<D,ID> function){
        Assert.notNull(function,"callback function must not be null");
        if(CollectionUtils.isEmpty(ids) || CollectionUtils.isEmpty(list)) return Collections.emptyMap();

        Map<ID,List<D>> result = new HashMap<>();
        list.forEach(data->result.computeIfAbsent(function.apply(data),id -> new LinkedList<>()).add(data));
        ids.forEach(id -> result.putIfAbsent(id,Collections.emptyList()));

        return result;
    }

    @NonNull
    public static <ID,D> Map<ID,D> convertTo(Collection<D> list,Function<D,ID> function){
        Assert.notNull(function,"callback function must not be null");
        if(CollectionUtils.isEmpty(list)) return Collections.emptyMap();

        Map<ID,D> result = new HashMap<>();
        list.forEach(data->result.putIfAbsent(function.apply(data),data));

        return result;
    }

    @NonNull
    public static <ID,D,V> Map<ID,V> convertTo(Collection<D> list,Function<D,ID> keyFunction,Function<D,V> valueFunction){
        Assert.notNull(keyFunction,"key callback function must not be null");
        if(CollectionUtils.isEmpty(list)) return Collections.emptyMap();

        Map<ID,V> result = new HashMap<>();
        list.forEach(data->result.putIfAbsent(keyFunction.apply(data),valueFunction.apply(data)));

        return result;
    }

    public static boolean idIsEmpty(@NonNull Number id){
        return id == null || id.longValue() <= 0;
    }
}
