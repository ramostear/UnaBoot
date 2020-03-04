package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.PostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName CategoryDetail
 * @Description 栏目详情
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 17:27
 * @Version since UnaBoot-1.0
 **/
@Service
public class CategoryDetail extends AbstractUnaBootDirectiveModel {
    @Autowired
    private PostCategoryService postCategoryService;
    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer category = handler.getInteger("category");
        if(category != null && category > 0){
            List<Post> data = postCategoryService.findAllPostByCategoryIdAndPostStatus(category, UnaBootConst.ACTIVE);
            if(CollectionUtils.isEmpty(data)){
                handler.put(MULTI,null).render();
            }else{
                handler.put(MULTI,data).render();
            }
        }
    }
}
