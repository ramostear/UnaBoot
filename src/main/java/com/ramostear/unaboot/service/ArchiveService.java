package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.vo.ArchiveVo;
import com.ramostear.unaboot.domain.vo.PostSimpleVo;

import java.util.List;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/30 0030 3:07.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface ArchiveService {
    List<ArchiveVo> archives();
    List<PostSimpleVo> posts(String name);
}
