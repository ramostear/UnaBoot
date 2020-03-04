package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.dto.PostMinDto;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName Latest
 * @Description 最新文章
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 18:17
 * @Version since UnaBoot-1.0
 **/
@Service
public class Latest extends AbstractUnaBootDirectiveModel {
    @Autowired
    private PostService postService;
    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer size = handler.getInteger("size",5);
        List<PostMinDto> data = postService.latest(size);
        handler.put(MULTI,data).render();
    }
}
