package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.PostStatus;
import com.ramostear.unaboot.domain.dto.CategoryFullDto;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.PostCategory;
import com.ramostear.unaboot.domain.vo.CategorySimpleVo;
import com.ramostear.unaboot.repository.CategoryRepository;
import com.ramostear.unaboot.repository.PostCategoryRepository;
import com.ramostear.unaboot.repository.PostRepository;
import com.ramostear.unaboot.service.PostCategoryService;
import com.ramostear.unaboot.util.ComponentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 14:19.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("postCategoryService")
public class PostCategoryServiceImpl extends BaseServiceImpl<PostCategory,Integer> implements PostCategoryService {

    private final PostCategoryRepository postCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostCategoryServiceImpl(PostCategoryRepository postCategoryRepository,CategoryRepository categoryRepository,
                                   PostRepository postRepository) {
        super(postCategoryRepository);
        this.postCategoryRepository = postCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }


    @Override
    public Map<Integer, List<Category>> convertCategoryToMapByPostIds(Collection<Integer> postIds) {
        if(CollectionUtils.isEmpty(postIds)){
            return Collections.emptyMap();
        }
        List<PostCategory> postCategories = postCategoryRepository.findAllByPostIdIn(postIds);
        Set<Integer> categoryIds = ComponentUtils.getIdentifiers(postCategories,PostCategory::getCategoryId);
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        Map<Integer,Category> categoryMap = ComponentUtils.convertTo(categories,Category::getId);
        Map<Integer,List<Category>> map = new HashMap<>();
        postCategories.forEach(postCategory -> map.computeIfAbsent(postCategory.getPostId(),postId->new LinkedList<>()).add(categoryMap.get(postCategory.getCategoryId())));
        return map;
    }


    @Override
    public List<Post> findAllPostByCategoryIdAndPostStatus(Integer categoryId, Integer status) {
        Set<Integer> postIds = postCategoryRepository.findAllPostIdByCategoryAndPostStatus(categoryId,status);
        return postRepository.findAllById(postIds);
    }

    @Override
    public Page<Post> pagePostByCategoryId(Integer categoryId, Pageable pageable) {
        Set<Integer> postIds = postCategoryRepository.findAllPostIdByCategoryAndPostStatus(categoryId, PostStatus.ACTIVE);
        return postRepository.findAllByIdIn(postIds,pageable);
    }

    @Override
    public PostCategory mergeOrCreated(Integer postId, Integer categoryId) {
        PostCategory postCategory = postCategoryRepository.findByPostId(postId);
        if(postCategory == null){
            postCategory = new PostCategory(postId,categoryId);
        }else{
            if(!postCategory.getCategoryId().equals(categoryId)){
                postCategory.setCategoryId(categoryId);
            }
        }
        postCategoryRepository.save(postCategory);
        return postCategory;
    }


    @Override
    public Integer findCategoryIdByPostId(Integer postId) {
        return postCategoryRepository.findCategoryByPost(postId);
    }

    @Override
    @Transactional
    public List<PostCategory> removeByPostId(Integer postId) {
        return postCategoryRepository.deleteByPostId(postId);
    }

    @Override
    @Transactional
    public List<PostCategory> removeByCategoryId(Integer categoryId) {
        return postCategoryRepository.deleteByCategoryId(categoryId);
    }

    @Override
    public List<CategoryFullDto> findAllCategoryPostCountDTO(Sort sort) {
        List<Category> categories = categoryRepository.findAll(sort);
        Map<Integer,Long> countMap = ComponentUtils.convertTo(postCategoryRepository.findAllCategoryDetail(PostStatus.ACTIVE), CategorySimpleVo::getId,CategorySimpleVo::getPostSize);
        return categories.stream()
                .map(category -> {
                    CategoryFullDto dto = new CategoryFullDto().convertFrom(category);
                    dto.setPostSize(countMap.getOrDefault(category.getId(),0L));
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public Post findTopPostByCategoryId(Integer categoryId) {
        Integer postId = postCategoryRepository.findLatestPostIdByCategory(categoryId);
        if(postId != null && postId > 0){
            return postRepository.findById(postId).orElseGet(null);
        }
        return null;
    }

    @Override
    public List<Post> findAllPostByCategoryId(Integer id) {
        List<PostCategory> pcs = postCategoryRepository.findAllByCategoryId(id);
        List<Integer> postIds = pcs.stream()
                .map(PostCategory::getPostId)
                .distinct().collect(Collectors.toList());
        return postRepository.findAllById(postIds);
    }
}
