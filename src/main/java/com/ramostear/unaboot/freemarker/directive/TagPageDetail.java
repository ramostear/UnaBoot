package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @ClassName TagPageDetail
 * @Description 标签内容分页
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 18:25
 * @Version since UnaBoot-1.0
 **/
@Service
public class TagPageDetail extends AbstractUnaBootDirectiveModel {
    @Autowired
    private PostTagService postTagService;
    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer id = handler.getInteger("id");
        Integer offset = handler.getInteger("offset",1);
        Integer size = handler.getInteger("size",15);
        Page<Post> data = postTagService.findAllPostByTagIdAndPostStatus(id, UnaBootConst.ACTIVE, PageRequest.of(offset-1,size, Sort.by(Sort.Direction.DESC,"createTime")));
        handler.put(MULTI,data).render();
    }
}
