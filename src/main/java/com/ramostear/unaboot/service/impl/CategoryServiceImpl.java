package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.exception.AlreadyExistException;
import com.ramostear.unaboot.common.exception.ForbiddenException;
import com.ramostear.unaboot.common.exception.NotFoundException;
import com.ramostear.unaboot.domain.dto.CategoryDto;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.valueobject.CategoryVo;
import com.ramostear.unaboot.repository.CategoryRepository;
import com.ramostear.unaboot.repository.PostCategoryRepository;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName CategoryServiceImpl
 * @Description 栏目服务类
 * @Author 树下魅狐
 * @Date 2020/2/14 0014 16:15
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends UnaBootServiceImpl<Category,Integer> implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,PostCategoryRepository postCategoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
        this.postCategoryRepository = postCategoryRepository;
    }

    @Override
    @Transactional
    public Category create(Category category) {
        Assert.notNull(category,"Category data must not be null");
        long count = categoryRepository.countByName(category.getName());
        if(count > 0){
            log.warn("category has exist:[{}]",category);
            throw new AlreadyExistException("category has exist.");
        }
        if(category.getPid() > 0){
            count = categoryRepository.countById(category.getPid());
            if(count <= 0){
                log.warn("parent category with id:[{}] was not found",category.getPid());
                throw new NotFoundException("Target category was not found");
            }
        }
        return super.create(category);
    }

    @Override
    public CategoryVo treeNodes(Sort sort) {
        Assert.notNull(sort,"Sort data must not be null");
        CategoryVo root = getRoot();
        List<Category> categories = findAll(sort);
        if(!CollectionUtils.isEmpty(categories)){
            convertToNodes(root,categories);
        }
        return root;
    }

    @Override
    @Cacheable(value = "category",key = "#slug")
    public Category getBySlug(String slug) {
        Assert.notNull(slug,"Category slug must not be null");
        return categoryRepository.getBySlug(slug).orElse(null);
    }

    @Override
    public Category getBySlugOfNonNull(String slug) {
        Assert.notNull(slug,"Category slug must not be null");
        return categoryRepository.getBySlug(slug)
                .orElseThrow(()->new NotFoundException("Category with slug="+slug+" was not found").setException(slug));
    }

    @Override
    public Category getByName(String name) {
        Assert.notNull(name,"Category name must not be null");
        return categoryRepository.getByName(name).orElse(null);
    }

    @Override
    @Transactional
    public void deleteCategoryAndRelationById(Integer id) {
        List<Category> categories = this.findByParent(id);
        if(categories != null && categories.size() > 0){
            throw new ForbiddenException("该栏目还存在子栏目，不允许删除!");
        }
        Set<Integer> postIds =  postCategoryRepository.findPostsByCategory(id);
        if(postIds != null && postIds.size() > 0){
            throw new ForbiddenException("该栏目下还有文章，不允许删除!");
        }
        delete(id);
    }

    @Override
    public CategoryDto convertTo(Category category) {
        Assert.notNull(category,"Category must not be null");
        return new CategoryDto().convertFrom(category);
    }

    @Override
    public List<CategoryDto> convertTo(Collection<Category> categories) {
        if(CollectionUtils.isEmpty(categories)){
            return Collections.emptyList();
        }
        return categories.stream().map(this::convertTo).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "category")
    public List<Category> navigation() {
        return categoryRepository.findAllByShowOrderBySortIdAsc(1);
    }

    @Override
    public List<Category> findByParent(Integer pid) {
        return categoryRepository.findAllByPidOrderBySortIdAsc(pid);
    }

    private CategoryVo getRoot(){
        CategoryVo root = new CategoryVo();
        root.setId(0);
        root.setName("网站根栏目");
        root.setChildren(new LinkedList<>());
        root.setPid(-1);
        return root;
    }

    private void convertToNodes(CategoryVo root,List<Category> categories){
        Assert.notNull(root,"Category root node must not be null");
        if(CollectionUtils.isEmpty(categories)){
            return;
        }
        List<Category> children = categories.stream()
                .filter(category -> Objects.equals(root.getId(),category.getPid()))
                .collect(Collectors.toList());
        children.forEach(category -> {
            CategoryVo node = new CategoryVo().convertFrom(category);
            if(CollectionUtils.isEmpty(root.getChildren())){
                root.setChildren(new LinkedList<>());
            }
            root.getChildren().add(node);
        });
        categories.removeAll(children);
        if(!CollectionUtils.isEmpty(root.getChildren())){
            root.getChildren().forEach(child->convertToNodes(child,categories));
        }
    }
}
