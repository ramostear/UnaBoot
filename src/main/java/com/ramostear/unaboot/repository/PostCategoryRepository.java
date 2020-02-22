package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.PostCategory;
import com.ramostear.unaboot.domain.valueobject.CategoryInfoVo;
import com.ramostear.unaboot.repository.support.UnaBootRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public interface PostCategoryRepository extends UnaBootRepository<PostCategory,Integer> {

    @NonNull
    @Query("select pc.cid from PostCategory as pc where pc.pid = :pid")
    Integer findCategoryByPost(@NonNull @Param("pid") Integer pid);

    @NonNull
    @Query("select pc.pid from PostCategory as pc where pc.cid = :cid")
    Set<Integer> findPostsByCategory(@NonNull @Param("cid")Integer cid);

    @NonNull
    @Query("select pc.pid from PostCategory as pc,Post as p where pc.cid = :cid and p.status = :status and p.id = pc.pid")
    Set<Integer> findPostsByCategoryAndPostStatus(@NonNull @Param("cid")Integer cid,@NonNull @Param("status")Integer status);

    @NonNull
    List<PostCategory> findAllByPidIn(@NonNull Iterable<Integer> ids);

    @NonNull
    PostCategory findByPid(@NonNull Integer id);

    @NonNull
    List<PostCategory> findAllByCid(@NonNull Integer id);

    @NonNull
    List<PostCategory> deleteByPid(@NonNull Integer id);

    @NonNull
    List<PostCategory> deleteByCid(@NonNull Integer id);

    @NonNull
    @Query("select new com.ramostear.unaboot.domain.valueobject.CategoryInfoVo(pc.cid,c.name,c.slug,count(pc.pid)) from PostCategory  as pc,Category as c ,Post as p where pc.cid = c.id and pc.pid = p.id and p.status = :status")
    List<CategoryInfoVo> getCategoryInfoList(@NonNull @Param("status")Integer status);

    @NonNull
    @Query(nativeQuery = true,value = "select p.id from posts p,post_category pc where pc.cid=?1 and pc.pid=p.id and p.status =1 order by p.create_time asc limit 1")
    Integer findLatestPostByCategory(@NonNull Integer category);
}
