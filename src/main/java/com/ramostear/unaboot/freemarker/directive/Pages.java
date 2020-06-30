package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.common.PageType;
import com.ramostear.unaboot.common.PostStatus;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.vo.PostSimpleVo;
import com.ramostear.unaboot.freemarker.AbstractTemplateDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostCategoryService;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.service.PostTagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 12:44.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service
public class Pages extends AbstractTemplateDirectiveModel {

    private final PostService postService;
    private final PostCategoryService postCategoryService;
    private final PostTagService postTagService;
    private static final String[] sortProperties = {"createTime","visits","stick","recommend"};

    Pages(PostService postService,PostCategoryService postCategoryService,PostTagService postTagService){
        this.postService = postService;
        this.postTagService = postTagService;
        this.postCategoryService = postCategoryService;
    }

    @Override
    public void exec(DirectiveHandler handler) throws Exception {
        int type = handler.getInteger("type",0);
        PageType pageType = PageType.obtain(type);
        wrapperObject(handler,pageType);
    }

    private void wrapperObject(DirectiveHandler handler, PageType pageType) throws Exception{
        int offset = handler.getInteger("offset",1);
        int size = handler.getInteger("size",15);
        if(pageType == PageType.CATEGORY){
            int categoryId = handler.getInteger("id");
            Page<Post> postPage = postCategoryService.pagePostByCategoryId(categoryId, PageRequest.of(offset-1,size, Sort.by(Sort.Direction.DESC,sortProperties)));
            handler.put(MULTI,postPage).render();
        }else if (pageType == PageType.TAG){
            int tagId = handler.getInteger("id");
            Page<Post> postPage = postTagService.findAllPostByTagIdAndPostStatus(tagId, PostStatus.ACTIVE,PageRequest.of(offset-1,size,Sort.by(Sort.Direction.DESC,sortProperties)));
            handler.put(MULTI,postPage).render();
        }else{
            int style = handler.getInteger("style",0);
            Page<PostSimpleVo> voPage = postService.page(style,PageRequest.of(offset-1,size,Sort.by(Sort.Direction.DESC,sortProperties)));
            handler.put(MULTI,voPage).render();
        }
    }
}
