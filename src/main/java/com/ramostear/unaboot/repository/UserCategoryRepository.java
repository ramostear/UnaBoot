package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.UserCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/17 0017 21:01.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface UserCategoryRepository extends BaseRepository<UserCategory,Integer> {

    List<UserCategory> findAllByUserId(Integer userId);

    @Query("select uc.categoryId from UserCategory as uc where uc.userId=:userId")
    List<Integer> findAllCategoryIdByUserId(@Param("userId") Integer userId);

    List<UserCategory> findAllByUserIdAndCategoryIdIn(Integer userId, Collection<Integer> categoryIds);

    void deleteAllByUserId(Integer userId);
}
