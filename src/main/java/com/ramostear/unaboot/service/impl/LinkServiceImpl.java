package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.Link;
import com.ramostear.unaboot.repository.LinkRepository;
import com.ramostear.unaboot.service.LinkService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName LinkServiceImpl
 * @Description 友情连接服务实现类
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 4:30
 * @Version since UnaBoot-1.0
 **/
@Service("linkService")
public class LinkServiceImpl extends UnaBootServiceImpl<Link,Integer> implements LinkService {
    private final LinkRepository linkRepository;
    public LinkServiceImpl(LinkRepository linkRepository) {
        super(linkRepository);
        this.linkRepository = linkRepository;
    }

    @Override
    @Cacheable(value = "link")
    public List<Link> findAll(Sort sort) {
        return super.findAll(sort);
    }
}
