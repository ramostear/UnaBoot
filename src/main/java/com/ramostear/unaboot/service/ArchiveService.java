package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.valueobject.ArchiveVo;
import com.ramostear.unaboot.domain.valueobject.PostSimpleVo;

import java.util.List;

public interface ArchiveService {

    List<ArchiveVo> archives();

    List<PostSimpleVo> archivePosts(String name);


}
