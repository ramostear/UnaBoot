package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.freemarker.AbstractTemplateDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 6:13.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service
public class SubNavMenus extends AbstractTemplateDirectiveModel {

    private final CategoryService categoryService;

    SubNavMenus(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    @Override
    public void exec(DirectiveHandler handler) throws Exception {
        int parent = handler.getInteger("pid",0);
        if(parent > 0){
            List<Category> categories = categoryService.findAllByPid(parent);
            if(CollectionUtils.isEmpty(categories)){
                handler.put(MULTI,null).render();
            }else{
                handler.put(MULTI,categories).render();
            }
        }else{
            handler.put(MULTI,null).render();
        }
    }
}
