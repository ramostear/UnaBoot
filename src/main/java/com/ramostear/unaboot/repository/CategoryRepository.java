package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.repository.support.UnaBootRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends UnaBootRepository<Category,Integer> {

    long countByName(@NonNull String name);

    long countById(@NonNull Integer id);

    Optional<Category> getBySlug(@NonNull String slug);

    Optional<Category> getByName(@NonNull String name);

    List<Category> findAllByShowOrderBySortIdAsc(Integer show);

    List<Category> findAllByPidOrderBySortIdAsc(Integer pid);
}
