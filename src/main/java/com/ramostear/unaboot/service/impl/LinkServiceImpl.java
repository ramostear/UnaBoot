package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.Link;
import com.ramostear.unaboot.repository.LinkRepository;
import com.ramostear.unaboot.service.LinkService;
import com.ramostear.unaboot.util.DateTimeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 19:45.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("linkService")
public class LinkServiceImpl extends BaseServiceImpl<Link,Integer> implements LinkService {

    private final LinkRepository linkRepository;

    public LinkServiceImpl(LinkRepository linkRepository) {
        super(linkRepository);
        this.linkRepository = linkRepository;
    }

    @Override
    public List<Link> findAll() {
        return super.findAll();
    }

    @Override
    @Transactional
    public Link update(Link link) {
        Link l = findByIdNonNull(link.getId());
        BeanUtils.copyProperties(link,l,"id","createTime","updateTime");
        l.setUpdateTime(DateTimeUtils.now());
        return linkRepository.save(l);
    }
}
