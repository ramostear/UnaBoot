package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName PostTags
 * @Description 文章标签
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 18:20
 * @Version since UnaBoot-1.0
 **/
@Service
public class PostTags extends AbstractUnaBootDirectiveModel {

    @Autowired
    private PostTagService postTagService;

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer id = handler.getInteger("id");
        List<Tag> data = postTagService.findAllTagByPostId(id);
        handler.put(MULTI,data).render();
    }
}
