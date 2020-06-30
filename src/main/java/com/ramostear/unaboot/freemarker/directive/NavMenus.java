package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.freemarker.AbstractTemplateDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 6:06.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service
public class NavMenus extends AbstractTemplateDirectiveModel {

    private final CategoryService categoryService;

    NavMenus(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @Override
    public void exec(DirectiveHandler handler) throws Exception {
        List<Category> menus = categoryService.navigation();
        handler.put(MULTI,menus).render();
    }
}
