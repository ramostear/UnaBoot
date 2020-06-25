package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Post;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/23 0023 20:12.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface LuceneService {

    int storage() throws IOException;

    boolean clear() throws IOException;

    Page<Post> findAll(String keyword, Pageable pageable) throws ParseException,IOException;

    boolean refresh();

}
