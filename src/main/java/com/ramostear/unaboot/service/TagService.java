package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.service.base.UnaBootService;
import org.springframework.lang.NonNull;

/**
 * @ClassName TagService
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 8:28
 * @Version since UnaBoot-1.0
 **/
public interface TagService extends UnaBootService<Tag,Integer> {
    @NonNull
    Tag findByName(@NonNull String name);

    @NonNull
    Tag createTag(@NonNull Tag tag);


}
