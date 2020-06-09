package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.PostCategory;
import com.ramostear.unaboot.domain.vo.CategorySimpleVo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 20:03.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface PostCategoryRepository extends BaseRepository<PostCategory,Integer> {

    @Query("select pc.categoryId from PostCategory as pc where pc.postId=:postId")
    Integer findCategoryByPost(@Param("postId")Integer postId);

    @Query("select pc.postId from PostCategory as pc where pc.categoryId=:categoryId")
    Set<Integer> findAllPostIdByCategory(@Param("categoryId")Integer categoryId);

    @Query("select pc.postId from PostCategory as pc,Post as p where pc.categoryId=:categoryId and p.status=:status and p.id=pc.postId")
    Set<Integer> findAllPostIdByCategoryAndPostStatus(@Param("categoryId")Integer categoryId,@Param("status")int status);

    List<PostCategory> findAllByPostIdIn(Iterable<Integer> postIds);

    PostCategory findByPostId(Integer postId);

    List<PostCategory> findAllByCategoryId(Integer categoryId);

    List<PostCategory> deleteByCategoryId(Integer categoryId);

    List<PostCategory> deleteByPostId(Integer postId);

    @Query("select new com.ramostear.unaboot.domain.vo.CategorySimpleVo(pc.categoryId,c.name,c.slug,count(pc.postId)) from " +
            "PostCategory  as pc,Category as c,Post as p where " +
            "pc.categoryId=c.id and pc.postId=p.id and p.status=:postStatus")
    List<CategorySimpleVo> findAllCategoryDetail(@Param("postStatus")int postStatus);

    @Query(value = "SELECT P.ID FROM POSTS AS P,POST_CATEGORY AS PC WHERE " +
            "PC.CATEGORY_ID=?1 AND PC.POST_ID=P.ID AND P.STATUS=1 " +
            "ORDER BY P.CREATE_TIME ASC LIMIT 1",nativeQuery = true)
    Integer findLatestPostIdByCategory(Integer categoryId);

}
