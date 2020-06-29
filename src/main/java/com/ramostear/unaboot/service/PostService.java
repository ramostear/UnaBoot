package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.dto.PostSimpleDto;
import com.ramostear.unaboot.domain.dto.PostSmallDto;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.vo.PostSimpleVo;
import com.ramostear.unaboot.domain.vo.PostVo;
import com.ramostear.unaboot.domain.vo.QueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 1:26.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface PostService extends BaseService<Post,Integer> {

    Page<Post> page(@NonNull QueryParam param, Pageable pageable);

    Page<Post> pageByUser(Integer userId,Pageable pageable);

    Page<Post> pageByStatus(Integer status,Pageable pageable);

    Page<Post> pageByUserAndStatus(Integer userId,Integer status,Pageable pageable);

    Page<PostSimpleVo> page(int style,Pageable pageable);

    Page<Post> page(@NonNull String key, Pageable pageable);

    Page<Post> draft(Integer userId,Pageable pageable);

    @Transactional
    PostVo createBy(Post post, Set<Integer> tagIds,Integer category,int status);

    @Transactional
    PostVo updateBy(Post post,Set<Integer> tagIds,Integer category,int status);

    @NonNull
    PostVo valueOf(@NonNull Post post);

    Page<PostSimpleVo> valueOf(Page<Post> source);

    PostSmallDto preOne(Integer id,Integer categoryId,Integer style);

    PostSmallDto nextOne(Integer id,Integer categoryId,Integer style);

    List<PostSmallDto> findAllAssociatedPosts(Integer id,int size);

    List<PostSmallDto> findAllMostVisitedPosts(int size);

    List<PostSmallDto> findAllLatestPosts(int size);

    List<PostSimpleVo> findAllStickPosts(int size);

    List<PostSimpleVo> findRecommendPosts(int size);

    Post findBySlug(String slug);

    List<Post> findAllByStatusIsPublished();

    List<PostSmallDto> findAllByCategory(Integer categoryId,int size);

    Long countByStatus(Integer status);

    Long countByUserId(Integer userId);

    Long countByUserIdAndStatus(Integer userId,Integer status);
}
