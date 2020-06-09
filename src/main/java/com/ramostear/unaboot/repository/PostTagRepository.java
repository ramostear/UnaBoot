package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.PostTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 20:04.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface PostTagRepository extends BaseRepository<PostTag,Integer>{

    List<PostTag> findAllByPostId(Integer postId);

    @Query("select pt.tagId from PostTag as pt where pt.postId=:postId")
    Set<Integer> findAllTagIdByPost(@Param("postId")Integer postId);

    List<PostTag> findAllByTagId(Integer tagId);

    @Query("select pt.postId from PostTag as pt where pt.tagId=:tagId")
    Set<Integer> findAllPostIdByTagId(@Param("tagId")Integer tagId);

    @Query("select pt.id from PostTag as pt,Post as p where " +
            "pt.tagId=:tagId and p.status=:status and p.id=pt.postId and p.style=0")
    Set<Integer> findAllPostByTagIdAndPostStatus(@Param("tagId")Integer tagId,@Param("status")Integer status);

    List<PostTag> findAllByPostIdIn(Iterable<Integer> postIds);

    List<PostTag> deleteByPostId(Integer postId);

    List<PostTag> deleteByTagId(Integer tagId);
}
