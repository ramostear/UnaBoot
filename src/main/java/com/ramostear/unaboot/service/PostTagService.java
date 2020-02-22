package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.PostTag;
import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.service.base.UnaBootService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PostTagService extends UnaBootService<PostTag,Integer> {

    @NonNull
    List<Tag> findAllTagByPostId(@NonNull Integer id);

    @NonNull
    List<Post> findAllPostByTagId(@NonNull Integer id);

    @NonNull
    List<Post> findAllPostByTagIdAndPostStatus(@NonNull Integer id,@NonNull Integer status);

    @NonNull
    Page<Post> findAllPostByTagId(@NonNull Integer id, @NonNull Pageable pageable);

    Page<Post> findAllPostByTagIdAndPostStatus(@NonNull Integer id,@NonNull Integer status,@NonNull Pageable pageable);

    @NonNull
    @Transactional
    List<PostTag> mergeOrCreateIfAbsent(@NonNull Integer postId, @NonNull Set<Integer> tagIds);

    @NonNull
    List<PostTag> findAllByPostId(@NonNull Integer id);

    @NonNull
    List<PostTag> findAllByTagId(@NonNull Integer id);

    @NonNull
    Set<Integer> findAllTagIdByPostId(@NonNull Integer id);

    @NonNull
    @Transactional
    List<PostTag> removeByPost(@NonNull Integer id);

    @NonNull
    @Transactional
    List<PostTag> removeByTag(@NonNull Integer id);

    @NonNull
    Map<Integer,List<Tag>> convertTagToMapByPost(@NonNull Collection<Integer> postIds);
}
