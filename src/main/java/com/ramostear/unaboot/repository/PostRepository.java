package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 20:04.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface PostRepository extends BaseRepository<Post,Integer>, JpaSpecificationExecutor<Post> {

    @NonNull
    Post findBySlug(@NonNull String slug);

    Post findBySlugAndStatus(String slug,Integer status);

    @Query(value="SELECT DATE_FORMAT(P.CREATE_TIME,'%Y-%m') AS NAME,COUNT(P.ID) AS COUNTS " +
            "FROM POSTS AS P " +
            "WHERE P.STATUS=?1 AND P.STYLE=0 " +
            "GROUP BY NAME ORDER BY NAME DESC ",nativeQuery = true)
    List<Object[]> findAllArchiveByStatus(Integer status);

    @Query(value = "SELECT P.* FROM POSTS AS P WHERE " +
            "DATE_FOMAT(P.CREATE_TIME,'%Y-%m')=?1 AND P.STATUS=?2 AND P.STYLE=0 " +
            "ORDER BY P.CREATE_TIME DESC",nativeQuery = true)
    List<Post> findAllPostByArchiveAndStatus(String archive,Integer status);


    @Query(value = "SELECT P.ID,P.TITLE,P.SLUG FROM POSTS AS P,POST_CATEGORY AS PC WHERE " +
            "P.ID <?1 AND P.STATUS=?2 AND P.ID=PC.POST_ID AND PC.CATEGORY_ID=?3 AND P.STYLE=?4 " +
            "ORDER BY P.ID DESC LIMIT 1",nativeQuery = true)
    List<Object[]> previous(Integer postId,Integer status,Integer categoryId,Integer style);

    @Query(value = "SELECT P.ID,P.TITLE,P.SLUG FROM POSTS AS P,POST_CATEGORY AS PC WHERE " +
            "P.ID>?1 AND P.STATUS=?2 AND P.ID=PC.POST_ID AND PC.CATEGORY_ID=?3 AND P.STYLE=?4 " +
            "ORDER BY P.ID ASC LIMIT 1",nativeQuery = true)
    List<Object[]> next(Integer postId,Integer status,Integer categoryId,Integer style);

    @Query(value = "SELECT P.* FROM POSTS AS P,TAG AS T,POST_TAG AS PT WHERE " +
            "T.ID IN(?1) AND P.ID=PT.POST_ID AND T.ID=PT.TAG_ID AND P.ID !=?2 AND P.STATUS=?3 AND P.STYLE=0 " +
            "AND P.ID >=(SELECT FLOOR(RAND()*((SELECT MAX(ID) FROM POSTS)-(SELECT MIN(ID) FROM POSTS))+(SELECT MIN(ID) FROM POSTS))) " +
            "ORDER BY P.CREATE_TIME DESC LIMIT ?4",nativeQuery = true)
    List<Post> findAllAssociatedPosts(List<Integer>tagIds,Integer postId,Integer status,int size);

    @Query(value = "SELECT P.* FROM POSTS AS P WHERE " +
            "P.STATUS=?1 AND P.STYLE=0 AND " +
            "P.VISITS >=(SELECT FLOOR(RAND()*((SELECT MAX(VISITS) FROM POSTS)-(SELECT MIN(VISITS) FROM POSTS))+(SELECT MIN(VISITS) FROM POSTS))) " +
            "ORDER BY P.VISITS DESC LIMIT ?2",nativeQuery = true)
    List<Post> findMostVisitedPosts(Integer status,Integer size);


    @Query(value = "SELECT P.* FROM POSTS AS P " +
            "WHERE P.STATUS=?1 AND P.STYLE=0 " +
            "ORDER BY P.CREATE_TIME DESC LIMIT ?2",nativeQuery = true)
    List<Post> findLatestPosts(Integer status,Integer size);

    @Query(value = "SELECT P.* FROM POSTS AS P WHERE " +
            "P.STATUS=?1 AND P.STYLE=0 AND P.STICK=1 AND " +
            "P.ID >=(SELECT FLOOR(RAND()*((SELECT MAX(ID) FROM POSTS)-(SELECT MIN(ID) FROM POSTS))+(SELECT MIN(ID) FROM POSTS))) " +
            "ORDER BY P.CREATE_TIME DESC LIMIT ?2",nativeQuery = true)
    List<Post> findStickPosts(Integer status,Integer size);

    @Query(value = "SELECT P.* FROM POSTS AS P WHERE " +
            "P.STATUS=?1 AND P.RECOMMEND=1 AND P.STYLE=0 AND " +
            "P.ID >=(SELECT FLOOR(RAND()*((SELECT MAX(ID) FROM POSTS)-(SELECT MIN(ID) FROM POSTS))+(SELECT MIN(ID) FROM POSTS))) " +
            "ORDER BY P.CREATE_TIME DESC LIMIT ?2",nativeQuery = true)
    List<Post> findRecommendPosts(Integer status,Integer size);

    @Query(value = "SELECT P.* FROM POSTS AS P,POST_CATEGORY AS PC WHERE " +
            "P.ID=PC.POST_ID AND PC.CATEGORY_ID=?1 AND P.STATUS=?2 " +
            "ORDER BY P.CREATE_TIME DESC LIMIT ?3",nativeQuery = true)
    List<Post> findByCategory(Integer categoryId,Integer status,Integer size);

    List<Post> findAllByStatusAndStyle(int status,int style);

    Page<Post> findAllByStatusAndStyle(int status, int style, Pageable pageable);
}
