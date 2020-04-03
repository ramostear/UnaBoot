package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.valueobject.PostSimpleVo;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @ClassName PostPageDetail
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/4/3 0003 17:22
 * @Version since UnaBoot-1.0
 **/
@Service
public class PostPageDetail extends AbstractUnaBootDirectiveModel {

    @Autowired
    private PostService postService;

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer type = handler.getInteger("type",0);
        Integer offset = handler.getInteger("offset",1);
        Integer size = handler.getInteger("size",15);
        Page<PostSimpleVo> data = postService.findAllByStyle(type, PageRequest.of(offset-1,size, Sort.by(Sort.Direction.DESC,"createTime")));
        handler.put(MULTI,data).render();
    }
}
