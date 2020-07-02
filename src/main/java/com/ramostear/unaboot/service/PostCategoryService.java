package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.dto.CategoryFullDto;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.PostCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 13:54.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface PostCategoryService extends BaseService<PostCategory,Integer> {


    @NonNull
    Map<Integer, List<Category>> convertCategoryToMapByPostIds(@NonNull Collection<Integer> postIds);

    @NonNull
    List<Post> findAllPostByCategoryIdAndPostStatus(@NonNull Integer categoryId,@NonNull Integer status);

    @NonNull
    Page<Post> pagePostByCategoryId(@NonNull Integer categoryId, @NonNull Pageable pageable);

    @NonNull
    PostCategory mergeOrCreated(@NonNull Integer postId,@NonNull Integer categoryId);

    @NonNull
    Integer findCategoryIdByPostId(@NonNull Integer postId);

    @NonNull
    List<PostCategory> removeByPostId(@NonNull Integer postId);

    @NonNull
    List<PostCategory> removeByCategoryId(@NonNull Integer categoryId);

    @NonNull
    List<CategoryFullDto> findAllCategoryPostCountDTO(@NonNull Sort sort);

    @NonNull
    Post findTopPostByCategoryId(@NonNull Integer categoryId);

    List<Post> findAllPostByCategoryId(Integer id);
}
