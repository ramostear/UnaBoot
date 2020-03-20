package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TagPosts
 * @Description 获取标签下的所有文章
 * @Author 树下魅狐
 * @Date 2020/3/19 0019 15:01
 * @Version since UnaBoot-1.0
 **/
@Service
public class TagPosts extends AbstractUnaBootDirectiveModel {

    @Autowired
    private PostTagService postTagService;

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer tagId = handler.getInteger("id");
        List<Post> data = postTagService.findAllPostByTagIdAndPostStatus(tagId, UnaBootConst.ACTIVE);
        handler.put(MULTI,data).render();
    }
}
