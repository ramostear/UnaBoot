package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.repository.TagRepository;
import com.ramostear.unaboot.service.PostTagService;
import com.ramostear.unaboot.service.TagService;
import com.ramostear.unaboot.util.DateTimeUtils;
import org.apache.shiro.util.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 14:23.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("tagService")
public class TagServiceImpl extends BaseServiceImpl<Tag,Integer> implements TagService {

    private final TagRepository tagRepository;
    private final PostTagService postTagService;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository,PostTagService postTagService) {
        super(tagRepository);
        this.tagRepository = tagRepository;
        this.postTagService = postTagService;
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        Tag t = tagRepository.findBySlug(tag.getSlug());
        Assert.isNull(t,"标签已经存在");
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public Tag update(Tag tag) {
        Tag original = tagRepository.findById(tag.getId()).orElse(null);
        if(original != null){
            BeanUtils.copyProperties(tag,original,"id","createTime","updateTime");
            original.setUpdateTime(DateTimeUtils.now());
            tagRepository.save(original);
            return original;
        }
        return null;
    }

    @Override
    @Transactional
    public Tag delete(Integer id) {
        postTagService.removeByTagId(id);
        return super.delete(id);
    }
}
