package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.repository.support.UnaBootRepository;

/**
 * @ClassName TagRepository
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 7:17
 * @Version since UnaBoot-1.0
 **/
public interface TagRepository extends UnaBootRepository<Tag,Integer> {
    Tag findByName(String name);
}
