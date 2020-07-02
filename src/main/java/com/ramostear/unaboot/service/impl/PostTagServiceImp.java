package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.PostTag;
import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.repository.PostRepository;
import com.ramostear.unaboot.repository.PostTagRepository;
import com.ramostear.unaboot.repository.TagRepository;
import com.ramostear.unaboot.service.PostTagService;
import com.ramostear.unaboot.util.ComponentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 12:59.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("postTagService")
public class PostTagServiceImp extends BaseServiceImpl<PostTag,Integer> implements PostTagService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostTagServiceImp(PostTagRepository postTagRepository,TagRepository tagRepository,
                             PostRepository postRepository) {
        super(postTagRepository);
        this.postTagRepository = postTagRepository;
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Tag> findAllTagByPostId(Integer id) {
        Set<Integer> tagIds = postTagRepository.findAllTagIdByPost(id);
        if(CollectionUtils.isEmpty(tagIds)){
            return Collections.emptyList();
        }
        return tagRepository.findAllById(tagIds);
    }

    @Override
    public List<Post> findAllPostByTagId(Integer id) {
        Set<Integer> postIds = postTagRepository.findAllPostIdByTagId(id);
        if(CollectionUtils.isEmpty(postIds)){
            return Collections.emptyList();
        }
        return postRepository.findAllById(postIds);
    }

    @Override
    @Cacheable(value = "tag",key = "#id")
    public List<Post> findAllPostByTagIdAndPostStatus(Integer id, int status) {
        Set<Integer> postIds = postTagRepository.findAllPostByTagIdAndPostStatus(id,status);
        if(CollectionUtils.isEmpty(postIds)){
            return Collections.emptyList();
        }
        return postRepository.findAllById(postIds);
    }

    @Override
    public Page<Post> findAllByTagId(Integer id, Pageable pageable) {
        Set<Integer> postIds = postTagRepository.findAllPostIdByTagId(id);
        if(CollectionUtils.isEmpty(postIds)){
            return Page.empty(pageable);
        }
        return postRepository.findAllByIdIn(postIds,pageable);
    }

    @Override
    public Page<Post> findAllPostByTagIdAndPostStatus(Integer id, int status, Pageable pageable) {
        Set<Integer> postIds = postTagRepository.findAllPostByTagIdAndPostStatus(id,status);
        if(CollectionUtils.isEmpty(postIds)){
            return Page.empty(pageable);
        }
        return postRepository.findAllByIdIn(postIds,pageable);
    }

    @Override
    @Transactional
    public List<PostTag> mergeOrCreateIfAbsent(Integer postId, Set<Integer> tagIds) {
        if(CollectionUtils.isEmpty(tagIds)){
            List<PostTag> postTags = postTagRepository.findAllByPostId(postId);
            if(!CollectionUtils.isEmpty(postTags)){
                postTagRepository.deleteInBatch(postTags);
            }
            return Collections.emptyList();
        }
        List<PostTag> postTags = tagIds.stream().map(tagId-> new PostTag(postId,tagId)).collect(Collectors.toList());
        List<PostTag> removeItems = new LinkedList<>(),saveItems = new LinkedList<>();
        List<PostTag> originalItems = postTagRepository.findAllByPostId(postId);
        originalItems.forEach(item->{
            if(!postTags.contains(item)){
                removeItems.add(item);
            }
        });
        postTags.forEach(item->{
            if(!originalItems.contains(item)){
                saveItems.add(item);
            }
        });
        super.delete(removeItems);
        originalItems.removeAll(removeItems);
        originalItems.addAll(super.create(saveItems));
        return originalItems;
    }

    @Override
    public List<PostTag> findAllByPostId(Integer id) {
        return postTagRepository.findAllByPostId(id);
    }

    @Override
    public List<PostTag> findAllByTagId(Integer id) {
        return postTagRepository.findAllByTagId(id);
    }

    @Override
    public Set<Integer> findTagIdsByPostId(Integer id) {
        return postTagRepository.findAllTagIdByPost(id);
    }

    @Override
    @Transactional
    public List<PostTag> removeByPostId(Integer id) {
        return postTagRepository.deleteByPostId(id);
    }

    @Override
    @Transactional
    public List<PostTag> removeByTagId(Integer id) {
        return postTagRepository.deleteByTagId(id);
    }

    @Override
    public Map<Integer, List<Tag>> toTagMapByPostId(Collection<Integer> postIds) {
        if(CollectionUtils.isEmpty(postIds)){
            return Collections.emptyMap();
        }
        List<PostTag> postTags = postTagRepository.findAllByPostIdIn(postIds);
        Set<Integer> tagIds = ComponentUtils.getIdentifiers(postTags,PostTag::getTagId);
        List<Tag> tags = tagRepository.findAllById(tagIds);
        Map<Integer,Tag> tagMap = ComponentUtils.convertTo(tags,Tag::getId);
        Map<Integer,List<Tag>> map = new HashMap<>();
        postTags.forEach(postTag -> map.computeIfAbsent(postTag.getPostId(),postId->new LinkedList<>())
                .add(tagMap.get(postTag.getTagId())));
        return map;
    }
}
