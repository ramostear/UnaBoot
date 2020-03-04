package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @ClassName CategoryPageDetail
 * @Description 带分页的栏目详细信息
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 17:32
 * @Version since UnaBoot-1.0
 **/
@Service
public class CategoryPageDetail extends AbstractUnaBootDirectiveModel {
    @Autowired
    private PostCategoryService postCategoryService;
    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer id = handler.getInteger("id");
        Integer offset = handler.getInteger("offset",1);
        Integer size = handler.getInteger("size",15);
        Page<Post> data = postCategoryService.pagePostByCategoryId(id, PageRequest.of(offset-1,size, Sort.by(Sort.Direction.DESC,"createTime")));
        handler.put(MULTI,data).render();
    }
}
