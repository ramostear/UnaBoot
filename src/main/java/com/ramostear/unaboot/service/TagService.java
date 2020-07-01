package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Tag;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 14:23.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface TagService extends BaseService<Tag,Integer> {

    Tag findBySlug(String slug);
}
