package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.PostTag;
import com.ramostear.unaboot.domain.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 2:41.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface PostTagService  extends BaseService<PostTag,Integer>{
    List<Tag> findAllTagByPostId(Integer id);

    List<Post> findAllPostByTagId(Integer id);

    List<Post> findAllPostByTagIdAndPostStatus(Integer id,int status);

    Page<Post> findAllByTagId(Integer id, Pageable pageable);

    Page<Post> findAllPostByTagIdAndPostStatus(Integer id,int status,Pageable pageable);

    List<PostTag> mergeOrCreateIfAbsent(Integer postId, Set<Integer> tagIds);

    List<PostTag> findAllByPostId(Integer id);

    List<PostTag> findAllByTagId(Integer id);

    Set<Integer> findTagIdsByPostId(Integer id);

    List<PostTag> removeByPostId(Integer id);

    List<PostTag> removeByTagId(Integer id);

    Map<Integer,List<Tag>> toTagMapByPostId(Collection<Integer> postIds);
}
