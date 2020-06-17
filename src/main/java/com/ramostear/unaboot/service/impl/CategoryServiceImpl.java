package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.component.FileManager;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.vo.CategoryVo;
import com.ramostear.unaboot.exception.ForbiddenException;
import com.ramostear.unaboot.repository.CategoryRepository;
import com.ramostear.unaboot.repository.PostCategoryRepository;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.service.PostCategoryService;
import com.ramostear.unaboot.util.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
    private final PostCategoryRepository postCategoryRepository;
    private final FileManager fileManager;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               PostCategoryRepository postCategoryRepository,
                               FileManager fileManager) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
        this.postCategoryRepository = postCategoryRepository;
        this.fileManager = fileManager;
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
    @Transactional
    public Category delete(Integer id) {
        Optional<Category> optional = categoryRepository.findById(id);
        if(!optional.isPresent()){
            throw new ForbiddenException("栏目不存在");
        }
        Category category = optional.get();
        List<Category> categories = this.findAllByPid(id);
        if(categories != null && categories.size()>0){
            throw new ForbiddenException("目录下还存在子目录，不允许删除");
        }
        Set<Integer> postIds = postCategoryRepository.findAllPostIdByCategory(id);
        if(postIds != null && postIds.size() > 0){
            throw new ForbiddenException("目录下还有文章，不允许删除");
        }
        String thumb = category.getThumb();
        if(StringUtils.isNotBlank(thumb)){
            fileManager.remove(thumb);
        }
        categoryRepository.delete(category);
        return category;
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
