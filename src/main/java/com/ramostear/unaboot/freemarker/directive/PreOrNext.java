package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.dto.PostMinDto;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName PreOrNext
 * @Description 上下篇
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 17:11
 * @Version since UnaBoot-1.0
 **/
@Service
public class PreOrNext extends AbstractUnaBootDirectiveModel {
    private static final String PREVIOUS = "previous",NEXT = "next";
    @Autowired
    private PostService postService;

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer postId = handler.getInteger("current");
        Integer category = handler.getInteger("category");
        String type = handler.getString("type");
        Integer style = handler.getInteger("style",0);
        PostMinDto data;
        if(type.equalsIgnoreCase(PREVIOUS)){
            data = postService.previous(postId,category,style);
        }else if(type.equalsIgnoreCase(NEXT)){
            data = postService.next(postId,category,style);
        }else{
            data = null;
        }
        handler.put(SINGLE,data).render();
    }
}
