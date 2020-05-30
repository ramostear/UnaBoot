package com.ramostear.unaboot.util;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 1:12.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class ComponentUtils {
    private ComponentUtils(){}

    /**
     * Get identifiers of multiple entities.
     * @param entities      target data
     * @param function      callback function interface
     * @param <ID>          Identifier type
     * @param <T>           Entity type
     * @return              Identifier array
     */
    @NonNull
    public static <ID,T> Set<ID> getIdentifiers(final Collection<T> entities, Function<T,ID> function){
        return CollectionUtils.isEmpty(entities)?
                Collections.emptySet():
                entities.stream().map(function).collect(Collectors.toSet());
    }

    /**
     * Convert the collection to a hash map with identifier as the key and domain list as the value.
     * @param ids           identifier collection
     * @param domains       domain collection
     * @param function      callback function interface
     * @param <ID>          identifier type
     * @param <D>           domain type
     * @return              hash map
     */
    @NonNull
    public static <ID,D>Map<ID, List<D>> convertTo(Collection<ID> ids,Collection<D> domains,Function<D,ID> function){
        Assert.notNull(function,"callback function must not be null.");
        if(CollectionUtils.isEmpty(ids) || CollectionUtils.isEmpty(domains)){
            return Collections.emptyMap();
        }
        Map<ID,List<D>> listMap = new HashMap<>();
        domains.forEach(domain -> listMap.computeIfAbsent(function.apply(domain),id -> new LinkedList<>()).add(domain));
        ids.forEach(id -> listMap.putIfAbsent(id,Collections.emptyList()));
        return listMap;
    }

    /**
     * Convert the domain collection to a hash map with identifier as key and domain as the value.
     * @param domains           domain collection
     * @param function          callback function interface
     * @param <ID>              identifier type
     * @param <D>               domain type
     * @return                  hashMap
     */
    @NonNull
    public static <ID,D> Map<ID,D> convertTo(Collection<D> domains,Function<D,ID>function){
        Assert.notNull(function,"callback function must not be null.");
        if(CollectionUtils.isEmpty(domains)){
            return Collections.emptyMap();
        }
        Map<ID,D> map = new HashMap<>();
        domains.forEach(domain->map.putIfAbsent(function.apply(domain),domain));
        return map;
    }

    /**
     * Covert domain collection to hash map with identifier as the key and any specified attribute value of the map value.
     * @param domains       domain collection
     * @param keyFun        key callback function
     * @param valueFun      value callback function
     * @param <ID>          identifier type
     * @param <D>           domain type
     * @param <V>           attribute value type
     * @return              hash map
     */
    @NonNull
    public static <ID,D,V> Map<ID,V> convertTo(Collection<D> domains,Function<D,ID> keyFun,Function<D,V> valueFun){
        Assert.notNull(keyFun,"key callback function must not be null");
        if(CollectionUtils.isEmpty(domains)){
            return Collections.emptyMap();
        }
        Map<ID,V> map = new HashMap<>();
        domains.forEach(domain->map.putIfAbsent(keyFun.apply(domain),valueFun.apply(domain)));
        return map;
    }
}
