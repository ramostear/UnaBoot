package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.repository.support.UnaBootRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends UnaBootRepository<Post,Integer> , JpaSpecificationExecutor<Post> {

    Post findBySlug(String slug);
    Post findBySlugAndStatus(String slug,Integer status);

    @Query(nativeQuery = true,value = "select DATE_FORMAT(p.create_time,'%Y年%m月') as name,COUNT(p.id) as counts from posts as p where p.status=?1 and p.style=0 group by name order by name desc ")
    List<Object[]> archives(Integer status);

    @Query(nativeQuery = true,value = "select p.* from posts as p where DATE_FORMAT(p.create_time,'%Y年%m月')=?1 and p.status=?2 and p.style=0 order by p.create_time desc")
    List<Post> archivePosts(String archiveName,Integer status);

    @Query(nativeQuery = true,value = "select p.id,p.title,p.slug from posts as p ,post_category as pc where p.id <?1 and p.status = ?2 and p.id = pc.pid and pc.cid = ?3 and p.style=?4 order by p.id desc limit 1")
    List<Object[]> previous(Integer postId,Integer status,Integer categoryId,Integer style);

    @Query(nativeQuery = true,value = "select p.id,p.title,p.slug from posts as p,post_category as pc where p.id >?1 and p.status = ?2 and p.id=pc.pid and pc.cid=?3 and p.style=?4 order by p.id asc limit 1")
    List<Object[]> next(Integer postId,Integer status,Integer categoryId,Integer style);

    @Query(nativeQuery = true,value = "select  p.* from posts as p,tags as t,post_tag as pt where t.id in(?1) and p.id = pt.pid and t.id = pt.tid and p.id !=?2 and p.status=?3 and p.style=0 group by p.id order by p.create_time desc,RAND() limit ?4")
    List<Post> associated(List<Integer> tags,Integer postId,Integer status,int size);

    @Query(nativeQuery = true,value = "select p.* from posts as p where p.status=?1 and p.style=0 order by p.visits desc limit ?2")
    List<Post> popularity(Integer status,Integer size);

    @Query(nativeQuery = true,value = "select p.* from posts as p where p.status=?1 and p.style=0 order by p.create_time desc limit ?2")
    List<Post> latest(Integer status,Integer size);

    @Query(nativeQuery = true,value="select p.* from posts as p where p.status=1 and p.stick=1 and p.style=0 order by p.create_time desc,RAND() limit ?1")
    List<Post> sticks(Integer size);

    @Query(nativeQuery = true,value = "select p.* from posts as p where p.status=1 and p.recommend = 1 and p.style = 0 order by p.create_time desc,RAND() asc limit ?1")
    List<Post> recommends(Integer size);

    List<Post> findAllByStatusAndStyle(int status,int style);
}
