package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.vo.CategoryVo;
import com.ramostear.unaboot.repository.CategoryRepository;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 1:47.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("categoryService")
public class CategoryServiceImpl extends BaseServiceImpl<Category,Integer> implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElse(defaultCategory());
    }

    @Override
    public CategoryVo tree(Sort sort) {
        CategoryVo root = initializeRoot();
        List<Category> list;
        if(sort == null){
            list = categoryRepository.findAll();
        }else{
            list = categoryRepository.findAll(sort);
        }
        if(!CollectionUtils.isEmpty(list)){
            initializeNodes(root,list);
        }
        return root;
    }

    @Override
    public List<Category> findAllByPid(Integer pid) {
        return categoryRepository.findAllByPidOrderBySortIdAsc(pid);
    }


    @Override
    @Transactional
    public Category update(Category category) {
        category.setUpdateTime(DateTimeUtils.now());
        return super.update(category);
    }

    @Override
    public Category delete(Integer id) {
        //TODO 需要解除关联关系
        return super.delete(id);
    }

    private Category defaultCategory(){
        Category category = new Category();
        category.setId(0);
        category.setPid(-1);
        category.setName("根栏目");
        category.setTheme("category");
        category.setPostTheme("post");
        return category;
    }

    private CategoryVo initializeRoot(){
        CategoryVo root = new CategoryVo();
        root.setId(0);
        root.setPid(-1);
        root.setName("栏目列表");
        root.setChildren(new LinkedList<>());
        return root;
    }

    private void initializeNodes(CategoryVo root, Collection<Category> data){
        if(!CollectionUtils.isEmpty(data)){
            List<Category> children = data.stream()
                    .filter(item-> Objects.equals(root.getId(),item.getPid()))
                    .collect(Collectors.toList());
            children.forEach(node->root.getChildren().add(new CategoryVo().convertFrom(node)));
            data.removeAll(children);
            if(!CollectionUtils.isEmpty(root.getChildren())){
                root.getChildren().forEach(item->initializeNodes(item,data));
            }
        }
    }
}
