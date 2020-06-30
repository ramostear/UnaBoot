package com.ramostear.unaboot.freemarker.methods;

import com.ramostear.unaboot.common.PageType;
import com.ramostear.unaboot.common.PostStatus;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import com.ramostear.unaboot.service.PostCategoryService;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.service.PostTagService;
import com.ramostear.unaboot.util.BeanUtils;
import freemarker.template.TemplateModelException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 16:11.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Component
public class PageUrl extends AbstractMethodModel {

    private final PostCategoryService postCategoryService;
    private final PostTagService postTagService;
    private final PostService postService;
    private static final String[] sortProperties = {"createTime","visits","stick","recommend"};

    PageUrl(PostCategoryService postCategoryService,PostTagService postTagService,
            PostService postService){
        this.postCategoryService = postCategoryService;
        this.postTagService = postTagService;
        this.postService = postService;
    }


    @Override
    public Object exec(List arguments) throws TemplateModelException {
        String url = getString(arguments,0);
        int id = getInteger(arguments,1);
        id = Math.max(id, 0);
        int offset = getInteger(arguments,2);
        offset = Math.max(offset, 1);
        int size = getInteger(arguments,3);
        size = size <=0?15:size;
        int spans = getInteger(arguments,4);
        spans = spans<=0?5:spans;
        int style = getInteger(arguments,5);
        style = Math.max(style, 0);
        Pageable pageable = PageRequest.of(offset-1,size, Sort.by(Sort.Direction.DESC,sortProperties));
        return initializePageUrl(url,id,style,spans,pageable);
    }

    private String initializePageUrl(String url, int id, int style, int spans, Pageable pageable) {
        PageType type = PageType.obtain(style);
        Page<Post> data;
        if(type == PageType.CATEGORY && id > 0){
            data = postCategoryService.pagePostByCategoryId(id,pageable);
        }else if (type == PageType.TAG && id > 0){
            data = postTagService.findAllPostByTagIdAndPostStatus(id, PostStatus.ACTIVE,pageable);
        }else{
            data = postService.page(style, pageable).map(item->{
                        Post post = new Post();
                        BeanUtils.copyProperties(item,post);
                        return post;
                    });
        }

        int span = (spans-3)/2;
        int pageNo = data.getNumber()+1;
        String curl;
        if(url.contains("?")){
            curl = url+"&offset=";
        }else{
            curl = url+"?offset=";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<ul class='pagination'>");
        if(pageNo > 1){
            int prev = pageNo -1;
            sb.append("<li class='page-item'>")
                    .append("<a class='page-link href='")
                    .append(curl)
                    .append(prev)
                    .append("' >Previous</a>")
                    .append("</li>");
        }else{
            sb.append("<li class='page-item disabled'>")
                    .append("<a class='page-link' href='javascript:void(0);' >Previous</a>")
                    .append("</li>");
        }
        int totalNo = (span*2)+3;
        int totalNo1 = totalNo -1;
        if(data.getTotalPages() > totalNo){
            if(pageNo <= (span+2)){
                for(int i=1;i<=totalNo1;i++){
                    pageLink(sb,pageNo,i,curl);
                }
                pageLink(sb,0,0,"javascript:void(0);");
                pageLink(sb,pageNo,data.getTotalPages(),curl);
            }else if (pageNo > (data.getTotalPages() - (span+2))){
                pageLink(sb,pageNo,1,curl);
                pageLink(sb,0,0,"javascript:void(0);");
                int num = data.getTotalPages() - totalNo + 2;
                for(int i=num;i<data.getTotalPages();i++){
                    pageLink(sb,pageNo,i,curl);
                }
            }else{
                pageLink(sb,pageNo,1,curl);
                pageLink(sb,0,0,"javascript:void(0);");
                int num = pageNo - span;
                int num2 = pageNo + span;
                for(int i=num;i<=num2;i++){
                    pageLink(sb,pageNo,i,curl);
                }
                pageLink(sb,0,0,"javascript:void(0);");
                pageLink(sb,pageNo,data.getTotalPages(),curl);
            }
        }else if (data.getTotalPages() > 1){
            for(int i=1;i<=data.getTotalPages();i++){
                pageLink(sb,pageNo,i,curl);
            }
        }else{
            pageLink(sb,1,1,curl);
        }

        if(pageNo < data.getTotalPages()){
            int next = pageNo +1;
            sb.append("<li class='page-item'>")
                    .append("<a class='page-link' href='")
                    .append(curl)
                    .append(next)
                    .append("'>Next</a>")
                    .append("</li>");
        }else{
            sb.append("<li class='page-item disabled'>")
                    .append("<a class='page-link' href='javascript:void(0);'>Next</a>")
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private void pageLink(StringBuilder sb,int pageNo,int index,String curl){
        if(index == 0){
            sb.append("<li><span style='margin:0 5px;letter-spacing:5px;font-size:16px'>...</span></li>");
        }else if(pageNo == index){
            sb.append("<li class='page-item active'>").append("<a class='page-link' href='javascript:void(0);'>").append(index).append("<span class='sr-only'>(current)</span></a>")
                    .append("</li>");
        }else{
            sb.append("<li class='page-item'>").append("<a class='page-link' href='").append(curl).append(index).append("'>").append(index).append("</a>")
                    .append("</li>");
        }
    }
}
