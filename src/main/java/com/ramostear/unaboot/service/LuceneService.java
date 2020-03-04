package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Post;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName LuceneService
 * @Description 基于Lucene的全文检索服务
 * @Author 树下魅狐
 * @Date 2020/3/5 0005 1:29
 * @Version since UnaBoot-1.0
 **/
public interface LuceneService {

    int addIndexs() throws IOException;

    boolean addOneIndex(Integer postId) throws IOException;

    boolean deleteAllIndex() throws IOException;

    boolean deleteOneIndex(Integer postId) throws IOException;

    boolean updateOneIndex(Integer postId) throws IOException;

    List<Post> queryAllByKeyword(String keyword) throws ParseException, IOException;

    Page<Post> findAll(String keyword, Pageable pageable) throws ParseException, IOException;

    boolean resetIndex();
}
