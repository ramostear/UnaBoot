package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName ChildCategory
 * @Description 子栏目
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 18:13
 * @Version since UnaBoot-1.0
 **/
@Service
public class ChildCategory extends AbstractUnaBootDirectiveModel {

    @Autowired
    private CategoryService categoryService;

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Integer root = handler.getInteger("root",0);
        if(root > 0){
            List<Category> data = categoryService.findByParent(root);
            if(CollectionUtils.isEmpty(data)){
                handler.put("data",null).render();
            }else{
                handler.put("data",data).render();
            }
        }else{
            handler.put("data",null).render();
        }
    }
}
