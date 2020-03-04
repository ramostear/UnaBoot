package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName Tags
 * @Description 标签
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 18:23
 * @Version since UnaBoot-1.0
 **/
@Service
public class Tags extends AbstractUnaBootDirectiveModel {

    @Autowired
    private TagService tagService;

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        List<Tag> data = tagService.findAll(Sort.by(Sort.Direction.DESC,"createTime"));
        handler.put(MULTI,data).render();
    }
}
