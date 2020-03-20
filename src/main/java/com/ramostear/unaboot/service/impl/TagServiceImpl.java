package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.repository.TagRepository;
import com.ramostear.unaboot.service.TagService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName TagServiceImpl
 * @Description 系统标签
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 8:33
 * @Version since UnaBoot-1.0
 **/
@Service("tagService")
public class TagServiceImpl extends UnaBootServiceImpl<Tag,Integer> implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        super(tagRepository);
        this.tagRepository = tagRepository;
    }

    @Override
    @Cacheable(value = "tag",key = "#name")
    public Tag findByName(String name) {
        Assert.notNull(name,"Tag name must not be null.");
        return tagRepository.findByName(name);
    }

    @Override
    public Tag createTag(Tag tag) {
        Assert.notNull(tag,"Tag is null");
        Assert.notNull(tag.getName(),"tag name is empty");
        Tag t = findByName(tag.getName());
        Assert.isNull(t,"tag already exist:"+tag.getName());
        return tagRepository.save(tag);
    }

    @Override
    public @NotNull Tag updateTag(@NotNull Tag tag) {
        Optional<Tag> optional = tagRepository.findById(tag.getId());
        if(optional.isPresent()){
            tag.setUpdateTime(DateTimeUtils.current());
            tagRepository.save(tag);
            return tag;
        }
        return null;
    }

    @Override
    @Cacheable(value = "tag")
    public List<Tag> findAll(Sort sort) {
        return super.findAll(sort);
    }
}
