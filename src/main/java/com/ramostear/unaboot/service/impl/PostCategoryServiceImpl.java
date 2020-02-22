package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.ServiceUtils;
import com.ramostear.unaboot.domain.dto.CategoryPostsDto;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.PostCategory;
import com.ramostear.unaboot.domain.valueobject.CategoryInfoVo;
import com.ramostear.unaboot.repository.CategoryRepository;
import com.ramostear.unaboot.repository.PostCategoryRepository;
import com.ramostear.unaboot.repository.PostRepository;
import com.ramostear.unaboot.service.PostCategoryService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName PostCategoryServiceImpl
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/23 0023 6:14
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service("postCategoryService")
public class PostCategoryServiceImpl extends UnaBootServiceImpl<PostCategory,Integer> implements PostCategoryService {

    private final PostCategoryRepository postCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    public PostCategoryServiceImpl(PostCategoryRepository postCategoryRepository,CategoryRepository categoryRepository,PostRepository postRepository) {
        super(postCategoryRepository);
        this.postCategoryRepository = postCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Optional<Category> findCategoryByPostId(Integer postId) {
        Integer categoryId = postCategoryRepository.findCategoryByPost(postId);
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Map<Integer, List<Category>> convertCategoryToMapByPostIds(Collection<Integer> postIds) {
        if(CollectionUtils.isEmpty(postIds)){
            return Collections.emptyMap();
        }
        List<PostCategory> postCategories = postCategoryRepository.findAllByPidIn(postIds);

        Set<Integer> categoryIds = ServiceUtils.fetchProperty(postCategories,PostCategory::getCid);

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        Map<Integer,Category> categoryMap = ServiceUtils.convertTo(categories,Category::getId);

        Map<Integer,List<Category>> result = new HashMap<>();

        postCategories.forEach(postCategory -> result.computeIfAbsent(postCategory.getPid(),postId->new LinkedList<>()).add(categoryMap.get(postCategory.getCid())));
        return result;
    }

    @Override
    public List<Post> findAllPostByCategoryId(Integer categoryId) {
        Assert.notNull(categoryId,"category id must not be null");
        Set<Integer> postIds = postCategoryRepository.findPostsByCategory(categoryId);
        return postRepository.findAllById(postIds);
    }

    @Override
    public List<Post> findAllPostByCategoryIdAndPostStatus(Integer categoryId, Integer status) {
        Assert.notNull(categoryId,"category id must not be null");
        Assert.notNull(status,"post status must not be null");
        Set<Integer> postIds = postCategoryRepository.findPostsByCategoryAndPostStatus(categoryId,status);
        return postRepository.findAllById(postIds);
    }

    @Override
    public Page<Post> pagePostByCategoryId(Integer categoryId, Pageable pageable) {
        Assert.notNull(categoryId,"category id must not be null");
        Assert.notNull(pageable,"pageable must not be null");
        Set<Integer> postIds = postCategoryRepository.findPostsByCategoryAndPostStatus(categoryId, UnaBootConst.ACTIVE);
        return postRepository.findAllByIdIn(postIds,pageable);
    }

    @Override
    @Transactional
    public PostCategory mergeOrCreated(Integer postId, Integer categoryId) {
        Assert.notNull(postId,"post id must not be null");
        Assert.notNull(categoryId,"category id must not be null");
        PostCategory postCategory = postCategoryRepository.findByPid(postId);
        if(postCategory == null){
            postCategory = new PostCategory();
            postCategory.setPid(postId);
            postCategory.setCid(categoryId);
        }else{
            if(postCategory.getCid().equals(categoryId)){
                postCategory.setCid(categoryId);
            }
        }
        postCategoryRepository.save(postCategory);
        return postCategory;
    }

    @Override
    public PostCategory findByPostId(Integer postId) {
        Assert.notNull(postId,"post id must not be null");
        return postCategoryRepository.findByPid(postId);
    }

    @Override
    public List<PostCategory> findAllByCategoryId(Integer categoryId) {
        Assert.notNull(categoryId,"category id must not be null");
        return postCategoryRepository.findAllByCid(categoryId);
    }

    @Override
    public Integer findCategoryIdByPostId(Integer postId) {
        Assert.notNull(postId,"post id must not be null");
        return postCategoryRepository.findCategoryByPost(postId);
    }

    @Override
    @Transactional
    public List<PostCategory> removeByPostId(Integer postId) {
        Assert.notNull(postId,"post id must not be null");
        return postCategoryRepository.deleteByPid(postId);
    }

    @Override
    @Transactional
    public List<PostCategory> removeByCategoryId(Integer categoryId) {
        Assert.notNull(categoryId,"Category id is empty.");
        return postCategoryRepository.deleteByCid(categoryId);
    }

    @Override
    public List<CategoryPostsDto> findAllCategoryPostCountDTO(Sort sort) {
        Assert.notNull(sort,"sort must not be null");
        List<Category> categories = categoryRepository.findAll(sort);
        Map<Integer,Long> countMap = ServiceUtils.convertTo(
                postCategoryRepository.getCategoryInfoList(UnaBootConst.ACTIVE),
                CategoryInfoVo::getId,CategoryInfoVo::getPosts);
        return categories.stream()
                .map(category -> {
                    CategoryPostsDto dto = new CategoryPostsDto().convertFrom(category);
                    dto.setPosts(countMap.getOrDefault(category.getId(),0L));
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public Post findTopPostByCategoryId(Integer categoryId) {
        Integer postId = postCategoryRepository.findLatestPostByCategory(categoryId);
        if(postId != null){
            return postRepository.findById(postId).orElseGet(null);
        }
        return null;
    }
}
