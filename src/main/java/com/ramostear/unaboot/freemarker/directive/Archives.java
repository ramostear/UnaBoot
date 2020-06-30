package com.ramostear.unaboot.freemarker.directive;

import com.ramostear.unaboot.domain.vo.ArchiveVo;
import com.ramostear.unaboot.freemarker.AbstractTemplateDirectiveModel;
import com.ramostear.unaboot.freemarker.DirectiveHandler;
import com.ramostear.unaboot.service.ArchiveService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 4:35.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service
public class Archives extends AbstractTemplateDirectiveModel {

    private final ArchiveService archiveService;

    Archives(ArchiveService archiveService){
        this.archiveService = archiveService;
    }

    @Override
    public void exec(DirectiveHandler handler) throws Exception {
        List<ArchiveVo> vos = archiveService.archives();
        handler.put(MULTI,vos).render();
    }
}
