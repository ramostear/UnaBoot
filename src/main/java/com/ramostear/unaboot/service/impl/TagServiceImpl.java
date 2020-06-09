package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.repository.TagRepository;
import com.ramostear.unaboot.service.TagService;
import com.ramostear.unaboot.util.DateTimeUtils;
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

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        super(tagRepository);
        this.tagRepository = tagRepository;
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
    public Tag delete(Integer integer) {
        //TODO 解除标签和文章的关系
        return super.delete(integer);
    }
}
