package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.common.PostType;
import com.ramostear.unaboot.domain.dto.PostSmallDto;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.freemarker.AbstractTemplateDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostService;
import org.springframework.stereotype.Service;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 15:58.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service
public class PostInfo extends AbstractTemplateDirectiveModel {

    private final PostService postService;

    PostInfo(PostService postService){
        this.postService = postService;
    }

    @Override
    public void exec(DirectiveHandler handler) throws Exception {
        int type = handler.getInteger("type",0);
        PostType postType = PostType.obtain(type);
        wrapperObject(handler,postType);
    }

    private void wrapperObject(DirectiveHandler handler, PostType type) throws Exception{
        int postId = handler.getInteger("id");
        if(type == PostType.PREVIOUS){
            int categoryId = handler.getInteger("category");
            int style = handler.getInteger("style",0);
            PostSmallDto dto = postService.preOne(postId,categoryId,style);
            handler.put(SINGLE,dto).render();
        }else if (type == PostType.NEXT){
            int categoryId = handler.getInteger("category");
            int style = handler.getInteger("style",0);
            PostSmallDto dto = postService.nextOne(postId,categoryId,style);
            handler.put(SINGLE,dto).render();
        }else{
            Post post = postService.findById(postId);
            handler.put(SINGLE,post).render();
        }
    }
}
