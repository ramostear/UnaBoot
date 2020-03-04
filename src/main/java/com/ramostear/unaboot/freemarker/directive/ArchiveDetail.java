package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.common.exception.UnaBootException;
import com.ramostear.unaboot.domain.valueobject.PostSimpleVo;
import com.ramostear.unaboot.freemarker.AbstractUnaBootDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.ArchiveService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName ArchiveDetail
 * @Description 归档详细内容
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 17:05
 * @Version since UnaBoot-1.0
 **/
@Service
public class ArchiveDetail extends AbstractUnaBootDirectiveModel {
    @Autowired
    private ArchiveService archiveService;
    @Override
    public void execute(DirectiveHandler handler) throws UnaBootException, TemplateException, IOException {
        String name = handler.getString("name");
        List<PostSimpleVo> data = archiveService.archivePosts(name);
        handler.put(MULTI,data).render();
    }
}
