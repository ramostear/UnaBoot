package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.dto.CategoryDto;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.valueobject.CategoryVo;
import com.ramostear.unaboot.service.base.UnaBootService;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;

public interface CategoryService extends UnaBootService<Category,Integer> {

    CategoryVo treeNodes(@NonNull Sort sort);

    @NonNull
    Category getBySlug(@NonNull String slug);

    @NonNull
    Category getBySlugOfNonNull(String slug);

    @NonNull
    Category getByName(@NonNull String name);

    void deleteCategoryAndRelationById(Integer id);

    @NonNull
    CategoryDto convertTo(@NonNull Category category);

    @NonNull
    List<CategoryDto> convertTo(@NonNull Collection<Category> categories);

    List<Category> navigation();

    @NonNull
    List<Category> findByParent(@NonNull Integer pid);
}
