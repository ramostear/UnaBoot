package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.UserCategory;

import java.util.List;
import java.util.Set;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/17 0017 20:54.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface UserCategoryService extends BaseService<UserCategory,Integer>{

    List<Integer> findAllCategoryByUserId(Integer id);

    List<UserCategory> mergeOrCreateIfAbsent(Integer userId, List<Integer> categoryIds);

    List<UserCategory> findAllByUserId(Integer userId);

    void removeBy(Integer userId,Set<Integer> categoryIds);

    void removeBy(Integer userId);
}
