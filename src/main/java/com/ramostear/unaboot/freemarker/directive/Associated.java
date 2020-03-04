package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.dto.PostMinDto;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName Associated
 * @Description 关联文章
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 17:08
 * @Version since UnaBoot-1.0
 **/
@Service
public class Associated extends AbstractUnaBootDirectiveModel {
    @Autowired
    private PostService postService;
    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer id = handler.getInteger("id");
        Integer size = handler.getInteger("size",4);
        List<PostMinDto> data = postService.associated(id,size);
        handler.put(MULTI,data).render();
    }
}
