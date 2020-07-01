package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.Category;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 1:46.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface CategoryRepository extends BaseRepository<Category,Integer> {
    List<Category> findAllByPidOrderBySortIdAsc(Integer pid);

    List<Category> findAllByNavShowOrderBySortIdAsc(int show);

    Optional<Category> getBySlug(@NonNull String slug);
}
