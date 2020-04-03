package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.dto.PostMinDto;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.valueobject.PostQuery;
import com.ramostear.unaboot.domain.valueobject.PostSimpleVo;
import com.ramostear.unaboot.domain.valueobject.PostVo;
import com.ramostear.unaboot.service.base.UnaBootService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface PostService extends UnaBootService<Post,Integer> {

    @NonNull
    Page<Post> pageBy(@NonNull PostQuery query, @NonNull Pageable pageable);

    Page<PostSimpleVo> findAllByStyle(int style,Pageable pageable);

    @NonNull
    Page<Post> search(@NonNull String key,@NonNull Pageable pageable);

    @NonNull
    @Transactional
    PostVo createBy(@NonNull Post post, Set<Integer> tagIds,Integer category,boolean autoSave);

    @NonNull
    @Transactional
    PostVo updateBy(@NonNull Post post, Set<Integer> tagIds,Integer category,boolean autoSave);

    @NonNull
    Post getBy(@NonNull String slug,@NonNull Integer status);

    boolean existBy(@NonNull String slug);

    @NonNull
    PostVo convert(@NonNull Post post);

    Page<PostSimpleVo> convert(@NonNull Page<Post> posts);

    PostMinDto previous(Integer id,Integer category,Integer style);

    PostMinDto next(Integer id,Integer category,Integer style);

    List<PostMinDto> associated(Integer id,Integer size);

    List<PostMinDto> popularity(Integer size);

    List<PostMinDto> latest(Integer size);

    List<PostSimpleVo> sticks(Integer size);

    List<PostSimpleVo> recommend(Integer size);

    Post findBySlug(String slug);

    @Transactional
    Post addVisits(Integer id);

    List<Post> findAllActive();
}
