package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.common.util.ServiceUtils;
import com.ramostear.unaboot.domain.dto.PostMinDto;
import com.ramostear.unaboot.domain.entity.*;
import com.ramostear.unaboot.domain.valueobject.PostQuery;
import com.ramostear.unaboot.domain.valueobject.PostSimpleVo;
import com.ramostear.unaboot.domain.valueobject.PostVo;
import com.ramostear.unaboot.repository.PostRepository;
import com.ramostear.unaboot.service.*;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.Assert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @ClassName PostServiceImpl
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/23 0023 6:09
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service("postService")
public class PostServiceImpl extends UnaBootServiceImpl<Post,Integer> implements PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final CategoryService categoryService;
    private final PostTagService postTagService;
    private final PostCategoryService postCategoryService;

    public PostServiceImpl(PostRepository postRepository,TagService tagService,CategoryService categoryService,PostTagService postTagService,PostCategoryService postCategoryService) {
        super(postRepository);
        this.postRepository = postRepository;
        this.tagService = tagService;
        this.categoryService = categoryService;
        this.postTagService = postTagService;
        this.postCategoryService = postCategoryService;
    }

    @Override
    public Page<Post> pageBy(PostQuery query, Pageable pageable) {
        return postRepository.findAll(buildSpecByQuery(query),pageable);
    }

    @Override
    public Page<Post> search(String key, Pageable pageable) {
        PostQuery query = new PostQuery();
        query.setKey(key);
        query.setStatus(UnaBootConst.ACTIVE);
        return postRepository.findAll(buildSpecByQuery(query),pageable);
    }

    @Override
    @Transactional
    public PostVo createBy(Post post, Set<Integer> tagIds, Integer category, boolean autoSave) {
        post.setStatus(autoSave?1:0);
        return createOrUpdate(post,tagIds,category);
    }

    @Override
    @Transactional
    public PostVo updateBy(Post post, Set<Integer> tagIds, Integer category, boolean autoSave) {
        post.setStatus(autoSave?1:0);
        post.setUpdateTime(DateTimeUtils.current());
        return createOrUpdate(post,tagIds,category);
    }

    @Override
    public Post getBy(String slug, Integer status) {
        return postRepository.findBySlugAndStatus(slug,status);
    }

    @Override
    public boolean existBy(String slug) {
        return postRepository.findBySlug(slug)!= null;
    }

    @Override
    public PostVo convert(Post post) {
        return convert(post,()->postTagService.findAllTagIdByPostId(post.getId()),postCategoryService.findCategoryIdByPostId(post.getId()));
    }

    @Override
    public Page<PostSimpleVo> convert(Page<Post> posts) {
        List<Post> postArray = posts.getContent();
        Set<Integer> postIds = ServiceUtils.fetchProperty(postArray,Post::getId);
        Map<Integer,List<Tag>> tagListMap = postTagService.convertTagToMapByPost(postIds);
        Map<Integer,List<Category>> categoryListMap = postCategoryService.convertCategoryToMapByPostIds(postIds);
        return posts.map(post->{
            PostSimpleVo vo = new PostSimpleVo().convertFrom(post);
            Optional.ofNullable(tagListMap.get(post.getId())).orElseGet(LinkedList::new);
            vo.setTags(Optional.ofNullable(tagListMap.get(post.getId()))
                .orElseGet(LinkedList::new)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
            );
            vo.setCategory(Optional.ofNullable(categoryListMap.get(post.getId()))
                .orElseGet(LinkedList::new)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                .get(0)
            );
            return vo;
        });
    }

    @Override
    public PostMinDto previous(Integer id, Integer category) {
        if(id == null || category == null){
            return null;
        }
        List<Object[]> data = postRepository.previous(id,UnaBootConst.ACTIVE,category);
        return convert(data);
    }

    @Override
    public PostMinDto next(Integer id, Integer category) {
        if(id == null || category == null){
            return null;
        }
        List<Object[]> data = postRepository.next(id,UnaBootConst.ACTIVE,category);
        return convert(data);
    }

    @Override
    public List<PostMinDto> associated(Integer id, Integer size) {
        Set<Integer> tagIds = postTagService.findAllTagIdByPostId(id);
        List<Post> posts = postRepository.associated(new ArrayList<>(tagIds),id,UnaBootConst.ACTIVE,size);
        return multiConvert(posts);
    }

    @Override
    public List<PostMinDto> popularity(Integer size) {
        List<Post> posts = postRepository.popularity(UnaBootConst.ACTIVE,size);
        return multiConvert(posts);
    }

    @Override
    public List<PostMinDto> latest(Integer size) {
        List<Post> posts = postRepository.latest(UnaBootConst.ACTIVE,size);
        return multiConvert(posts);
    }

    @Override
    public List<PostSimpleVo> sticks(Integer size) {
        List<Post> data = postRepository.sticks(size);
        return convertToSimpleVo(data);
    }

    @Override
    public List<PostSimpleVo> recommend(Integer size) {
        List<Post> data = postRepository.recommends(size);
        return convertToSimpleVo(data);
    }

    @Override
    public Post findBySlug(String slug) {
        if(StringUtils.isBlank(slug)){
            return null;
        }
        return postRepository.findBySlug(slug);
    }

    @Override
    @Transactional
    public Post addVisits(Integer id) {
        Optional<Post> optional = postRepository.findById(id);
        if(optional.isPresent()){
            Post post = optional.get();
            post.setVisits(post.getVisits()+1);
            return postRepository.save(post);
        }
        return null;
    }

    @Override
    public List<Post> findAllActive() {
        return postRepository.findAllByStatus(UnaBootConst.ACTIVE);
    }

    @NonNull
    private Specification<Post> buildSpecByQuery(PostQuery postQuery) {
        Assert.notNull(postQuery,"Query param object is empty");
        return (Specification<Post>)(root,query,builder)->{
            List<Predicate> predicates = new LinkedList<>();
            if(postQuery.getStatus() != null && postQuery.getStatus() > -2){
                predicates.add(builder.equal(root.get("status"),postQuery.getStatus()));
            }
            if(postQuery.getCategory() != null && postQuery.getCategory() > 0){
                Subquery<Post> postSubquery = query.subquery(Post.class);
                Root<PostCategory> postCategoryRoot = postSubquery.from(PostCategory.class);
                postSubquery.select(postCategoryRoot.get("pid"));
                postSubquery.where(
                        builder.equal(root.get("id"),postCategoryRoot.get("pid")),
                        builder.equal(postCategoryRoot.get("cid"),postQuery.getCategory())
                );
                predicates.add(builder.exists(postSubquery));
            }
            if(StringUtils.isNotEmpty(postQuery.getKey())){
                String condition = String.format("%%%s%%",StringUtils.strip(postQuery.getKey()));
                Predicate title = builder.like(root.get("title"),condition);
                Predicate summary = builder.like(root.get("summary"),condition);
                predicates.add(builder.or(title,summary));
            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    private PostVo createOrUpdate(@NonNull Post post,Set<Integer> tagIds,Integer category){
        Assert.notNull(post,"Post is empty");
        Category c = categoryService.findById(category);
        Assert.notNull(c,"Category is not exist");
        super.create(post);
        List<Tag> tags = tagService.findAllById(tagIds);
        List<PostTag> postTags = postTagService.mergeOrCreateIfAbsent(post.getId(), ServiceUtils.fetchProperty(tags,Tag::getId));
        log.info("create post and tag relationship:[{}]",postTags);
        PostCategory postCategory = postCategoryService.mergeOrCreated(post.getId(),c.getId());
        log.info("create post and category relationship:[{}]",postCategory);
        return convert(post,()->ServiceUtils.fetchProperty(postTags,PostTag::getId),c.getId());

    }

    private PostVo convert(@NonNull Post post, @NonNull Supplier<Set<Integer>> tagSupplier,@NonNull Integer category){
        PostVo postVo = new PostVo().convertFrom(post);
        postVo.setTagIds(tagSupplier == null? Collections.emptySet():tagSupplier.get());
        postVo.setCategory(category);
        return postVo;
    }

    private List<PostSimpleVo> convertToSimpleVo(List<Post> data){
        Set<Integer> postIds = ServiceUtils.fetchProperty(data,Post::getId);
        Map<Integer,List<Tag>> tagListMap = postTagService.convertTagToMapByPost(postIds);
        Map<Integer,List<Category>> categoryListMap = postCategoryService.convertCategoryToMapByPostIds(postIds);
        return data.stream().map(post->{
            PostSimpleVo vo = new PostSimpleVo().convertFrom(post);
            vo.setTags(tagListMap.get(post.getId()));
            vo.setCategory(categoryListMap.get(post.getId()).get(0));
            return vo;
        }).collect(Collectors.toList());


    }

    private PostMinDto convert(List<Object[]> data){
        PostMinDto dto = new PostMinDto();
        if(data != null && data.size() > 0){
            Object[] objects = data.get(0);
            Integer id = Integer.parseInt(objects[0].toString());
            String title = objects[1].toString();
            String slug = objects[2].toString();
            dto.setId(id);
            dto.setTitle(title);
            dto.setSlug(slug);
        }
        return dto;
    }

    private List<PostMinDto> multiConvert(List<Post> posts){
        return posts.stream().map(post->{
            PostMinDto dto = new PostMinDto().convertFrom(post);
            return dto;
        }).collect(Collectors.toList());
    }
}
