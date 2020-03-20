package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.ServiceUtils;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.PostTag;
import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.repository.PostRepository;
import com.ramostear.unaboot.repository.PostTagRepository;
import com.ramostear.unaboot.repository.TagRepository;
import com.ramostear.unaboot.service.PostTagService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import org.apache.shiro.util.Assert;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName PostTagServiceImpl
 * @Description 文章标签关联
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 11:08
 * @Version since UnaBoot-1.0
 **/
@Service("postTagService")
public class PostTagServiceImpl extends UnaBootServiceImpl<PostTag,Integer> implements PostTagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final PostRepository postRepository;


    public PostTagServiceImpl(TagRepository tagRepository,PostRepository postRepository,PostTagRepository postTagRepository) {
        super(postTagRepository);
        this.postTagRepository = postTagRepository;
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Tag> findAllTagByPostId(Integer id) {
        Assert.notNull(id,"Post id is empty");
        Set<Integer> tagIds = postTagRepository.findAllTagIdByPost(id);
        if(CollectionUtils.isEmpty(tagIds)){
            return Collections.emptyList();
        }
        return tagRepository.findAllById(tagIds);
    }

    @Override
    public List<Post> findAllPostByTagId(Integer id) {
        Assert.notNull(id,"Tag id is empty.");
        Set<Integer> postIds = postTagRepository.findAllPostByTagAndPostStatus(id, UnaBootConst.ACTIVE);
        if(CollectionUtils.isEmpty(postIds)){
            return Collections.emptyList();
        }
        return postRepository.findAllById(postIds);
    }

    @Override
    @Cacheable(value = "tag",key = "#id")
    public List<Post> findAllPostByTagIdAndPostStatus(Integer id, Integer status) {
        Assert.notNull(id,"Tag id is empty.");
        Set<Integer> postIds = postTagRepository.findAllPostByTagAndPostStatus(id,status);
        if(CollectionUtils.isEmpty(postIds)){
            return Collections.emptyList();
        }
        return postRepository.findAllById(postIds);
    }

    @Override
    public Page<Post> findAllPostByTagId(Integer id, Pageable pageable) {
        Assert.notNull(id,"Tag id is empty.");
        Set<Integer> postIds = postTagRepository.findAllPostIdByTag(id);
        if(CollectionUtils.isEmpty(postIds)){
            return Page.empty(pageable);
        }
        return postRepository.findAllByIdIn(postIds,pageable);
    }

    @Override
    @Cacheable(value = "tagPage",key = "#id")
    public Page<Post> findAllPostByTagIdAndPostStatus(Integer id, Integer status, Pageable pageable) {
        Assert.notNull(id,"Tag id is empty");
        Set<Integer> postIds = postTagRepository.findAllPostByTagAndPostStatus(id,status);
        if(CollectionUtils.isEmpty(postIds)){
            return Page.empty(pageable);
        }
        return postRepository.findAllByIdIn(postIds,pageable);
    }

    @Override
    @Transactional
    public List<PostTag> mergeOrCreateIfAbsent(Integer postId, Set<Integer> tagIds) {
        Assert.notNull(postId,"Post id must not be null");
        if(CollectionUtils.isEmpty(tagIds)){
            return Collections.emptyList();
        }
        List<PostTag> postTags = tagIds.stream().map(tagId->{
            PostTag pt = new PostTag();
            pt.setPid(postId);
            pt.setTid(tagId);
            return pt;
        }).collect(Collectors.toList());

        List<PostTag> removeArray = new LinkedList<>(),saveArray = new LinkedList<>();

        List<PostTag> originalArray = postTagRepository.findAllByPid(postId);
        originalArray.forEach(pt->{
            if(!postTags.contains(pt)){
                removeArray.add(pt);
            }
        });
        postTags.forEach(pt->{
            if(!originalArray.contains(pt)){
                saveArray.add(pt);
            }
        });
        super.delete(removeArray);
        originalArray.removeAll(removeArray);
        originalArray.addAll(super.createInBatch(saveArray));
        return originalArray;
    }

    @Override
    public List<PostTag> findAllByPostId(Integer id) {
        return postTagRepository.findAllByPid(id);
    }

    @Override
    public List<PostTag> findAllByTagId(Integer id) {
        return postTagRepository.findAllByTid(id);
    }

    @Override
    public Set<Integer> findAllTagIdByPostId(Integer id) {
        return postTagRepository.findAllTagIdByPost(id);
    }

    @Override
    @Transactional
    public List<PostTag> removeByPost(Integer id) {
        return postTagRepository.deleteByPid(id);
    }

    @Override
    public List<PostTag> removeByTag(Integer id) {
        return postTagRepository.deleteByTid(id);
    }

    @Override
    public Map<Integer, List<Tag>> convertTagToMapByPost(Collection<Integer> postIds) {
        if(CollectionUtils.isEmpty(postIds)){
            return Collections.emptyMap();
        }
        List<PostTag> pts = postTagRepository.findAllByPidIn(postIds);
        Set<Integer> tagIds = ServiceUtils.fetchProperty(pts,PostTag::getTid);
        List<Tag> tags = tagRepository.findAllById(tagIds);
        Map<Integer,Tag> tagMap = ServiceUtils.convertTo(tags,Tag::getId);
        Map<Integer,List<Tag>> tagListMap = new HashMap<>();
        pts.forEach(postTag -> tagListMap.computeIfAbsent(postTag.getPid(),postId->new LinkedList<>()).add(tagMap.get(postTag.getTid())));
        return tagListMap;
    }
}
