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
 * @ClassName Navigation
 * @Description 导航
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 17:23
 * @Version since UnaBoot-1.0
 **/
@Service
public class Navigation extends AbstractUnaBootDirectiveModel {

    @Autowired
    private CategoryService categoryService;

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        List<Category> data = categoryService.navigation();
        if(CollectionUtils.isEmpty(data)){
            handler.put(MULTI,null).render();
        }else{
            handler.put(MULTI,data).render();
        }
    }
}
