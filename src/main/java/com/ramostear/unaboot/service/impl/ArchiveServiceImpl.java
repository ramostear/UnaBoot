package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.PostStatus;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.domain.vo.ArchiveVo;
import com.ramostear.unaboot.domain.vo.PostSimpleVo;
import com.ramostear.unaboot.repository.PostRepository;
import com.ramostear.unaboot.service.ArchiveService;
import com.ramostear.unaboot.service.PostCategoryService;
import com.ramostear.unaboot.service.PostTagService;
import com.ramostear.unaboot.util.ComponentUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 3:10.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("archiveService")
public class ArchiveServiceImpl implements ArchiveService {

    private final PostRepository postRepository;
    private final PostTagService postTagService;
    private final PostCategoryService postCategoryService;

    ArchiveServiceImpl(PostRepository postRepository,PostTagService postTagService,
                       PostCategoryService postCategoryService){
        this.postRepository = postRepository;
        this.postTagService = postTagService;
        this.postCategoryService = postCategoryService;
    }

    @Override
    @Cacheable(value = "archives")
    public List<ArchiveVo> archives() {
        List<Object[]> data = postRepository.findAllArchiveByStatus(PostStatus.ACTIVE);
        List<ArchiveVo> vos = new ArrayList<>();
        if(!CollectionUtils.isEmpty(data)){
            data.forEach(item->{
                Object[] objects = item;
                ArchiveVo vo = new ArchiveVo();
                vo.setName(objects[0].toString());
                vo.setCounts(Long.valueOf(objects[1].toString()));
                vos.add(vo);
            });
        }
        return vos;
    }

    @Override
    @Cacheable(value = "archives",key = "#name")
    public List<PostSimpleVo> posts(String name) {
        if(StringUtils.isBlank(name)){
            return Collections.emptyList();
        }
        return convert(postRepository.findAllPostByArchiveAndStatus(name,PostStatus.ACTIVE));
    }

    private List<PostSimpleVo> convert(List<Post> data){
        Set<Integer> postIds = ComponentUtils.getIdentifiers(data,Post::getId);
        Map<Integer,List<Tag>> tags = postTagService.toTagMapByPostId(postIds);
        Map<Integer,List<Category>> categories = postCategoryService.convertCategoryToMapByPostIds(postIds);
        return data.stream()
                .map(post->{
                    PostSimpleVo vo = new PostSimpleVo().convertFrom(post);
                    return getPostSimpleVo(tags, categories, post, vo);
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
