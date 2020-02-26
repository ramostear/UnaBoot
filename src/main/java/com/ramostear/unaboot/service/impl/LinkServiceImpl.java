package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.Link;
import com.ramostear.unaboot.repository.LinkRepository;
import com.ramostear.unaboot.service.LinkService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @ClassName LinkServiceImpl
 * @Description TODO
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
}
