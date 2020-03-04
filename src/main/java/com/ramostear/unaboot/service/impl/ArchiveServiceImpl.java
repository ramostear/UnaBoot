package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.ServiceUtils;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.domain.valueobject.ArchiveVo;
import com.ramostear.unaboot.domain.valueobject.PostSimpleVo;
import com.ramostear.unaboot.repository.PostRepository;
import com.ramostear.unaboot.service.ArchiveService;
import com.ramostear.unaboot.service.PostCategoryService;
import com.ramostear.unaboot.service.PostTagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ArchiveServiceImpl
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 16:47
 * @Version since UnaBoot-1.0
 **/
@Service("archiveService")
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostTagService postTagService;
    @Autowired
    private PostCategoryService postCategoryService;

    @Override
    public List<ArchiveVo> archives() {
        List<Object[]> data = postRepository.archives(UnaBootConst.ACTIVE);
        List<ArchiveVo> result = new ArrayList<>();
        if(!CollectionUtils.isEmpty(data)){
            data.forEach(item->{
                Object[] objects = item;
                ArchiveVo vo = new ArchiveVo();
                vo.setName(objects[0].toString());
                vo.setCounts(Long.valueOf(objects[1].toString()));
                result.add(vo);
            });
        }
        return result;
    }

    @Override
    public List<PostSimpleVo> archivePosts(String name) {
        if(StringUtils.isBlank(name)){
            return Collections.emptyList();
        }
        return convertTo(postRepository.archivePosts(name,UnaBootConst.ACTIVE));
    }

    @NonNull
    private List<PostSimpleVo> convertTo(@NonNull List<Post> data){
        Set<Integer> postIds = ServiceUtils.fetchProperty(data,Post::getId);
        Map<Integer,List<Tag>> tags = postTagService.convertTagToMapByPost(postIds);
        Map<Integer,List<Category>> categories = postCategoryService.convertCategoryToMapByPostIds(postIds);
        return data.stream()
                .map(post -> {
                    PostSimpleVo vo = new PostSimpleVo().convertFrom(post);
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
                }).collect(Collectors.toList());
    }
}
