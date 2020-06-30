package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.freemarker.AbstractTemplateDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostTagService;
import com.ramostear.unaboot.service.TagService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 6:00.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service
public class Tags extends AbstractTemplateDirectiveModel {

    private final TagService tagService;
    private final PostTagService postTagService;

    Tags(TagService tagService,PostTagService postTagService){
        this.tagService = tagService;
        this.postTagService = postTagService;
    }

    @Override
    public void exec(DirectiveHandler handler) throws Exception {
        int postId = handler.getInteger("id",0);
        List<Tag> tags;
        if(postId <=0){
            tags = tagService.findAll(Sort.by(Sort.Direction.DESC,"createTime"));
        }else{
            tags = postTagService.findAllTagByPostId(postId);
        }
        handler.put(MULTI,tags).render();
    }
}
