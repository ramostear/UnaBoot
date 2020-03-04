package com.ramostear.unaboot.freemarker.function;

import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.freemarker.AbstractMethodModel;
import com.ramostear.unaboot.service.PostCategoryService;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName CategoryPage
 * @Description 栏目分页条目
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 17:37
 * @Version since UnaBoot-1.0
 **/
@Service
public class CategoryPage extends AbstractMethodModel {

    @Autowired
    private PostCategoryService postCategoryService;

    @Override
    public Object exec(List args) throws TemplateModelException {
        String url = getString(args,0);
        Integer id = getInteger(args,1);
        Integer offset = getInteger(args,2)!= null? getInteger(args,2):1;
        Integer spans = getInteger(args,3)!= null?getInteger(args,3):5;
        Integer size = getInteger(args,4) != null?getInteger(args,4):15;
        Page<Post> data = postCategoryService.pagePostByCategoryId(id, PageRequest.of(offset-1,size, Sort.by(Sort.Direction.DESC,"createTime")));
        int span = (spans-3)/2;
        int pageNo = data.getNumber()+1;
        String curl;
        if(url.contains("?")){
            curl = url+"&offset=";
        }else{
            curl = url+"?offset=";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<ul class='pagination'>");
        if(pageNo > 1){
            int prev = pageNo-1;
            sb.append("<li class='page-item'>").append("<a class='page-link' href='").append(curl).append(prev).append("'>Previous</a>")
                    .append("</li>");
        }else{
            sb.append("<li class='page-item disabled'>")
                    .append("<a class='page-link' href='javascript:void(0);'>Previous</a>")
                    .append("</li>");
        }
        int totalNo = (span*2)+3;
        int totalNo1 = totalNo-1;

        if(data.getTotalPages() > totalNo){
            if(pageNo <= (span+2)){
                for(int i=1;i<=totalNo1;i++){
                    pageLink(sb,pageNo,i,curl);
                }
                pageLink(sb,0,0,"javascript:void(0);");
                pageLink(sb,pageNo,data.getTotalPages(),curl);
            }else if(pageNo>(data.getTotalPages()-(span+2))){
                pageLink(sb,pageNo,1,curl);
                pageLink(sb,0,0,"javascript:void(0);");
                int num = data.getTotalPages() - totalNo + 2;
                for(int i=num;i<=data.getTotalPages();i++){
                    pageLink(sb,pageNo,i,curl);
                }
            }else{
                pageLink(sb,pageNo,1,curl);
                pageLink(sb,0,0,"javascript:void(0);");
                int num = pageNo-span;
                int num2 = pageNo + span;
                for(int i=num;i<=num2;i++){
                    pageLink(sb,pageNo,i,curl);
                }
                pageLink(sb,0,0,"javascript:void(0);");
                pageLink(sb,pageNo,data.getTotalPages(),curl);
            }
        }else if(data.getTotalPages() > 1){
            for(int i=1;i<=data.getTotalPages();i++){
                pageLink(sb,pageNo,i,curl);
            }
        }else{
            pageLink(sb,1,1,curl);
        }

        if(pageNo < data.getTotalPages()){
            int next = pageNo+1;
            sb.append("<li class='page-item'>").append("<a class='page-link' href='").append(curl).append(next).append("'>Next</a>")
                    .append("</li>");
        }else{
            sb.append("<li class='page-item disabled'>")
                    .append("<a class='page-link' href='javascript:void(0);'>Next</a>")
                    .append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private void pageLink(StringBuffer sb,int pageNo,int index,String curl){
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
