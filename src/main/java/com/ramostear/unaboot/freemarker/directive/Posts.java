package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.common.ParserType;
import com.ramostear.unaboot.common.PostStatus;
import com.ramostear.unaboot.domain.dto.PostSmallDto;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.vo.PostSimpleVo;
import com.ramostear.unaboot.freemarker.AbstractTemplateDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.ArchiveService;
import com.ramostear.unaboot.service.PostCategoryService;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.service.PostTagService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 5:02.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service
public class Posts extends AbstractTemplateDirectiveModel {

    private final PostService postService;
    private final ArchiveService archiveService;
    private final PostTagService postTagService;
    private final PostCategoryService postCategoryService;

    Posts(PostService postService,ArchiveService archiveService,
          PostTagService postTagService,PostCategoryService postCategoryService){
        this.postService = postService;
        this.archiveService = archiveService;
        this.postTagService = postTagService;
        this.postCategoryService = postCategoryService;
    }


    @Override
    public void exec(DirectiveHandler handler) throws Exception {
        int type = handler.getInteger("type",-1);
        ParserType parserType = ParserType.obtain(type);
        wrapperObject(handler,parserType);
    }

    private void wrapperObject(DirectiveHandler handler,ParserType type) throws Exception {
        if(type == ParserType.ARCHIVE){
            String name = handler.getString("name");
            List<PostSimpleVo> vos = archiveService.posts(name);
            handler.put(MULTI,vos).render();
        }else if(type == ParserType.LATEST){
            int size = handler.getInteger("size",5);
            List<PostSmallDto>  dtoList = postService.findAllLatestPosts(size);
            handler.put(MULTI,dtoList).render();
        }else if(type == ParserType.POPULAR){
            int size = handler.getInteger("size",5);
            List<PostSmallDto> dtoList = postService.findAllMostVisitedPosts(size);
            handler.put(MULTI,dtoList).render();
        }else if(type == ParserType.SIMILAR){
            int id = handler.getInteger("id");
            int size = handler.getInteger("size",5);
            List<PostSmallDto> dtoList = postService.findAllAssociatedPosts(id,size);
            handler.put(MULTI,dtoList).render();
        }else if (type == ParserType.SUGGEST){
            int size = handler.getInteger("size",5);
            List<PostSimpleVo> vos = postService.findRecommendPosts(size);
            handler.put(MULTI,vos).render();
        }else if (type == ParserType.TOP){
            int size = handler.getInteger("size",5);
            List<PostSimpleVo> vos = postService.findAllStickPosts(size);
            handler.put(MULTI,vos).render();
        }else if (type == ParserType.TAG){
            int tagId = handler.getInteger("id");
            List<Post> postList = postTagService.findAllPostByTagIdAndPostStatus(tagId, PostStatus.ACTIVE);
            handler.put(MULTI,postList).render();
        }else if (type == ParserType.CATEGORY){
            int id = handler.getInteger("id");
            if(id > 0){
                List<Post> postList = postCategoryService.findAllPostByCategoryIdAndPostStatus(id,PostStatus.ACTIVE);
                if(CollectionUtils.isEmpty(postList)){
                    handler.put(MULTI,null).render();
                }else{
                    handler.put(MULTI,postList).render();
                }
            }else{
                handler.put(MULTI,null).render();
            }
        }else{
            handler.put(MULTI,null).render();
        }
    }
}
