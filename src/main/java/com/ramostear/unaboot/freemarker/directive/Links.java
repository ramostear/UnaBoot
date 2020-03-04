package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.entity.Link;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName Links
 * @Description 友情连接
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 18:10
 * @Version since UnaBoot-1.0
 **/
@Service
public class Links extends AbstractUnaBootDirectiveModel {
    @Autowired
    private LinkService linkService;
    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        List<Link> data = linkService.findAll(Sort.by(Sort.Direction.ASC,"sortId"));
        handler.put(MULTI,data).render();
    }
}
