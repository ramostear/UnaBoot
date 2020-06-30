package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.entity.Link;
import com.ramostear.unaboot.freemarker.AbstractTemplateDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.LinkService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 5:57.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service
public class Links extends AbstractTemplateDirectiveModel {

    private final LinkService linkService;

    Links(LinkService linkService){
        this.linkService = linkService;
    }

    @Override
    public void exec(DirectiveHandler handler) throws Exception {
        List<Link> links = linkService.findAll(Sort.by(Sort.Direction.DESC,"sortId"));
        handler.put(MULTI,links).render();
    }
}
