package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.ContentType;
import com.ramostear.unaboot.common.PostStatus;
import com.ramostear.unaboot.domain.dto.PostSmallDto;
import com.ramostear.unaboot.domain.entity.*;
import com.ramostear.unaboot.domain.vo.PostSimpleVo;
import com.ramostear.unaboot.domain.vo.PostVo;
import com.ramostear.unaboot.domain.vo.QueryParam;
import com.ramostear.unaboot.repository.PostRepository;
import com.ramostear.unaboot.service.*;
import com.ramostear.unaboot.util.ComponentUtils;
import com.ramostear.unaboot.util.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 2:07.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("postService")
public class PostServiceImpl extends BaseServiceImpl<Post,Integer> implements PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final CategoryService categoryService;
    private final PostTagService postTagService;
    private final PostCategoryService postCategoryService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, TagService tagService,
                           CategoryService categoryService, PostTagService postTagService,
                           PostCategoryService postCategoryService) {
        super(postRepository);
        this.postRepository = postRepository;
        this.tagService = tagService;
        this.categoryService = categoryService;
        this.postTagService = postTagService;
        this.postCategoryService = postCategoryService;
    }

    @Override
    public Page<Post> page(QueryParam param, Pageable pageable) {
        return postRepository.findAll(specificationQuery(param),pageable);
    }

    @Override
    public Page<Post> pageByUser(Integer userId, Pageable pageable) {
        return postRepository.findAllByUserId(userId,pageable);
    }

    @Override
    public Page<Post> pageByStatus(Integer status, Pageable pageable) {
        return postRepository.findAllByStatus(status,pageable);
    }

    @Override
    public Page<Post> pageByUserAndStatus(Integer userId, Integer status, Pageable pageable) {
        return postRepository.findAllByUserIdAndStatus(userId,status,pageable);
    }

    @Override
    public Page<PostSimpleVo> page(int style, Pageable pageable) {
        Page<Post> data = postRepository.findAllByStatusAndStyle(PostStatus.ACTIVE,style,pageable);
        return  this.valueOf(data);
    }

    @Override
    public Page<Post> page(String key, Pageable pageable) {
        QueryParam param = new QueryParam();
        param.setKey(key);
        param.setStatus(PostStatus.ACTIVE);
        return postRepository.findAll(specificationQuery(param),pageable);
    }

    @Override
    public Page<Post> draft(Integer userId, Pageable pageable) {
        return postRepository.findAllByUserIdAndStatus(userId, PostStatus.DRAFT,pageable);
    }

    @Override
    @Transactional
    public PostVo createBy(Post post, Set<Integer> tagIds, Integer category, int status) {
        post.setStatus(status);
        return createOrUpdate(post,tagIds,category);
    }

    @Override
    @Transactional
    public PostVo updateBy(Post post, Set<Integer> tagIds, Integer category, int status) {
        post.setStatus(status);
        post.setUpdateTime(DateTimeUtils.now());
        return createOrUpdate(post,tagIds,category);
    }

    @Override
    public PostVo valueOf(Post post) {
        return convert(post,()->postTagService.findTagIdsByPostId(post.getId()),postCategoryService.findCategoryIdByPostId(post.getId()));
    }

    @Override
    public Page<PostSimpleVo> valueOf(Page<Post> source) {
        List<Post> posts = source.getContent();
        Set<Integer> postIds = ComponentUtils.getIdentifiers(posts,Post::getId);
        Map<Integer,List<Tag>> tagMap = postTagService.toTagMapByPostId(postIds);
        Map<Integer,List<Category>> categoryMap = postCategoryService.convertCategoryToMapByPostIds(postIds);
        return source.map(post->{
            PostSimpleVo simpleVo = new PostSimpleVo().convertFrom(post);
            Optional.ofNullable(tagMap.get(post.getId())).orElseGet(LinkedList::new);
            return getPostSimpleVo(tagMap, categoryMap, post, simpleVo);
        });
    }

    @Override
    public PostSmallDto preOne(Integer id, Integer categoryId, Integer style) {
        if(id == null || categoryId == null){
            return null;
        }
        List<Object[]> resultSet = postRepository.previous(id,PostStatus.ACTIVE,categoryId,style);
        return convert(resultSet);
    }

    @Override
    public PostSmallDto nextOne(Integer id, Integer categoryId, Integer style) {
        if(id == null || categoryId == null){
            return null;
        }
        List<Object[]> resultSet = postRepository.next(id,PostStatus.ACTIVE,categoryId,style);
        return convert(resultSet);
    }

    @Override
    public List<PostSmallDto> findAllAssociatedPosts(Integer id, int size) {
        List<Integer> tagIds = new ArrayList<>(postTagService.findTagIdsByPostId(id));
        List<Post> posts = postRepository.findAllAssociatedPosts(tagIds,id,PostStatus.ACTIVE,size);
        return multiConvert(posts);
    }

    @Override
    public List<PostSmallDto> findAllMostVisitedPosts(int size) {
        return multiConvert(postRepository.findMostVisitedPosts(PostStatus.ACTIVE,size));
    }

    @Override
    public List<PostSmallDto> findAllLatestPosts(int size) {
        return multiConvert(postRepository.findLatestPosts(PostStatus.ACTIVE,size));
    }

    @Override
    public List<PostSimpleVo> findAllStickPosts(int size) {
        return simpleConvert(postRepository.findStickPosts(PostStatus.ACTIVE,size));
    }

    @Override
    public List<PostSimpleVo> findRecommendPosts(int size) {
        return simpleConvert(postRepository.findRecommendPosts(PostStatus.ACTIVE,size));
    }

    @Override
    @Cacheable(value = "posts",key = "#slug")
    public Post findBySlug(String slug) {
        if(StringUtils.isBlank(slug)){
            return null;
        }else{
            return postRepository.findBySlug(slug);
        }
    }

    @Override
    public List<Post> findAllByStatusIsPublished() {
        return postRepository.findAllByStatusAndStyle(PostStatus.ACTIVE, ContentType.POST);
    }

    @Override
    public List<PostSmallDto> findAllByCategory(Integer categoryId, int size) {
        return multiConvert(postRepository.findByCategory(categoryId,PostStatus.ACTIVE,size));
    }

    @Override
    public Long countByStatus(Integer status) {
        return postRepository.countByStatus(status);
    }

    @Override
    public Long countByUserId(Integer userId) {
        return postRepository.countByUserId(userId);
    }

    @Override
    public Long countByUserIdAndStatus(Integer userId, Integer status) {
        return postRepository.countByUserIdAndStatus(userId,status);
    }

    private Specification<Post> specificationQuery(QueryParam param){
        return (Specification<Post>)(root,query,builder)->{
            List<Predicate> predicates = new LinkedList<>();
            if(param.getStatus() != null && param.getStatus() > -10){
                predicates.add(builder.equal(root.get("status"),param.getStatus()));
            }
            if(param.getStatus() != null && param.getStyle() > -1){
                predicates.add(builder.equal(root.get("style"),param.getStyle()));
            }
            if(param.getUserId() != null && param.getUserId() > 0){
                predicates.add(builder.equal(root.get("userId"),param.getUserId()));
            }
            if(param.getCategory() != null && param.getCategory() > 0){
                Subquery<Post> postSubQuery = query.subquery(Post.class);
                Root<PostCategory> postCategoryRoot = postSubQuery.from(PostCategory.class);
                postSubQuery.select(postCategoryRoot.get("postId"));
                postSubQuery.where(
                        builder.equal(root.get("id"),postCategoryRoot.get("postId")),
                        builder.equal(postCategoryRoot.get("categoryId"),param.getCategory())
                );
                predicates.add(builder.exists(postSubQuery));

            }
            if(StringUtils.isNotEmpty(param.getKey())){
                String condition = "%"+param.getKey()+"%";
                Predicate title = builder.like(root.get("title"),condition);
                Predicate summary = builder.like(root.get("summary"),condition);
                Predicate keywords = builder.like(root.get("keywords"),condition);
                predicates.add(builder.or(title,summary,keywords));
            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    private PostVo createOrUpdate(Post post,Set<Integer> tagIds,Integer categoryId){
        Category category = categoryService.findById(categoryId);
        post.setTpl(category.getPostTheme());
        postRepository.save(post);
        List<Tag> tags = tagService.findAll(tagIds);
        List<PostTag> postTags = postTagService.mergeOrCreateIfAbsent(post.getId(), ComponentUtils.getIdentifiers(tags,Tag::getId));
        postCategoryService.mergeOrCreated(post.getId(),category.getId());
        return convert(post,()->ComponentUtils.getIdentifiers(postTags,PostTag::getTagId),category.getId());
    }


    private PostVo convert(Post post, Supplier<Set<Integer>> tagSupplier,Integer categoryId){
        PostVo postVo = new PostVo().convertFrom(post);
        postVo.setTagIds(tagSupplier==null? Collections.emptySet():tagSupplier.get());
        postVo.setCategory(categoryId);
        return postVo;
    }

    private PostSmallDto convert(List<Object[]> resultSet){
        PostSmallDto dto = new PostSmallDto();
        if(resultSet != null && resultSet.size() > 0){
            Object[] objects = resultSet.get(0);
            Integer id = Integer.parseInt(objects[0].toString());
            String title = objects[1].toString();
            String slug = objects[2].toString();
            dto.setId(id);
            dto.setTitle(title);
            dto.setSlug(slug);
        }
        return dto;
    }

    private List<PostSmallDto> multiConvert(List<Post> posts){
        return posts.stream().map(post->{
            PostSmallDto dto = new PostSmallDto().convertFrom(post);
            return dto;
        }).collect(Collectors.toList());
    }

    private List<PostSimpleVo> simpleConvert(List<Post> posts){
        Set<Integer> postIds = ComponentUtils.getIdentifiers(posts,Post::getId);
        Map<Integer,List<Tag>> tagMap = postTagService.toTagMapByPostId(postIds);
        Map<Integer,List<Category>> categoryMap = postCategoryService.convertCategoryToMapByPostIds(postIds);
        return posts.stream()
                .map(post->{
                    PostSimpleVo simpleVo = new PostSimpleVo().convertFrom(post);
                    simpleVo.setTags(tagMap.get(post.getId()));
                    simpleVo.setCategory(categoryMap.get(post.getId()).get(0));
                    return simpleVo;
                }).collect(Collectors.toList());
    }

    private static PostSimpleVo getPostSimpleVo(Map<Integer, List<Tag>> tags, Map<Integer, List<Category>> categories, Post post, PostSimpleVo vo) {
        vo.setTags(Optional.ofNullable(tags.get(post.getId()))
                .orElseGet(LinkedList::new)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        vo.setCategory(Optional.ofNullable(categories.get(post.getId()))
                .orElseGet(LinkedList::new)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList()).get(0));
        return vo;
    }
}
