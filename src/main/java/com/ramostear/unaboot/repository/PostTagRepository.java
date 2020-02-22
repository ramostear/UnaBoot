package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.PostTag;
import com.ramostear.unaboot.repository.support.UnaBootRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public interface PostTagRepository extends UnaBootRepository<PostTag,Integer> {

    @NonNull
    List<PostTag> findAllByPid(@NonNull Integer pid);

    @NonNull
    @Query("select pt.tid from PostTag as pt where pt.pid = :pid")
    Set<Integer> findAllTagIdByPost(@NonNull @Param("pid")Integer pid);

    @NonNull
    List<PostTag> findAllByTid(@NonNull Integer tid);

    @NonNull
    @Query("select pt.pid from PostTag as pt where pt.tid = :tid")
    Set<Integer> findAllPostIdByTag(@NonNull @Param("tid")Integer tid);

    @Query("select pt.pid from PostTag as pt,Post as p where pt.tid = :tid and p.status = :status and p.id = pt.pid")
    Set<Integer> findAllPostByTagAndPostStatus(@NonNull @Param("tid")Integer tid,@NonNull @Param("status")Integer status);

    @NonNull
    List<PostTag> findAllByPidIn(@NonNull Iterable<Integer> ids);

    @NonNull
    List<PostTag> deleteByPid(@NonNull Integer pid);

    @NonNull
    List<PostTag> deleteByTid(@NonNull Integer tid);
}
