package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.vo.CategoryVo;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 1:46.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface CategoryService extends BaseService<Category,Integer> {

    CategoryVo tree(Sort sort);

    List<Category> findAllByPid(Integer pid);

    List<Category> navigation();
}
