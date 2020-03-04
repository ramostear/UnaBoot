package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.service.LuceneService;
import com.ramostear.unaboot.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName LuceneServiceImpl
 * @Description 全文检索
 * @Author 树下魅狐
 * @Date 2020/3/5 0005 1:33
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service("luceneService")
public class LuceneServiceImpl implements LuceneService {
    //分词器
    private static StandardAnalyzer analyzer;
    //索引库
    private static Directory directory;
    //分词器工具
    private IndexWriterConfig config = null;
    //流
    private IndexWriter indexWriter = null;
    static {
        analyzer = new StandardAnalyzer();
        try {
            directory = FSDirectory.open(Paths.get(UnaBootConst.FILE_UPLOAD_ROOT_DIR+"lucene"));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    @Autowired
    private PostService postService;

    @Override
    public int addIndexs() throws IOException {
        int count = 0;
        List<Post> data = postService.findAllActive();
        if(!CollectionUtils.isEmpty(data)){
            ArrayList<Document> documents = new ArrayList<>();
            data.forEach(post->{
                documents.add(wrapperDocument(post));
            });
            config = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory,config);
            indexWriter.addDocuments(documents);
            try {
                indexWriter.commit();
                count = data.size();
            }catch (Exception e){
                log.error(e.getMessage());
                indexWriter.rollback();
            }finally {
                indexWriter.close();
                indexWriter = null;
                config = null;
            }
        }
        return count;
    }

    @Override
    public boolean addOneIndex(Integer postId) throws IOException {
        boolean flag = false;
        Post p = postService.findById(postId);
        if(p == null || p.getStatus()!=UnaBootConst.ACTIVE){
            return false;
        }
        Document document = wrapperDocument(p);
        config = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(directory,config);
        indexWriter.addDocument(document);
        try {
            indexWriter.commit();
            flag = true;
        }catch (Exception e){
            log.error(e.getMessage());
            indexWriter.rollback();
        }finally {
            indexWriter.close();
            indexWriter = null;
            config = null;
        }
        return flag;
    }

    @Override
    public boolean deleteAllIndex() throws IOException {
        boolean flag = false;
        config = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(directory,config);
        try {
            indexWriter.deleteAll();
            indexWriter.commit();
            flag = true;
        }catch (Exception e){
            log.error(e.getMessage());
            indexWriter.rollback();
        }finally {
            indexWriter.close();
            indexWriter = null;
            config = null;
        }
        return flag;
    }

    @Override
    public boolean deleteOneIndex(Integer postId) throws IOException {
        boolean flag = false;
        Term term = new Term("id",String.valueOf(postId));
        config = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(directory,config);
        try {
            indexWriter.deleteDocuments(term);
            indexWriter.commit();
            flag = true;
        }catch (Exception e){
            log.error(e.getMessage());
            indexWriter.rollback();
        }finally {
            indexWriter.close();
            indexWriter = null;
            config = null;
        }
        return flag;
    }

    @Override
    public boolean updateOneIndex(Integer postId) throws IOException {
        boolean flag =false;
        Post p = postService.findById(postId);
        if(p == null || p.getStatus()!=UnaBootConst.ACTIVE){
            return false;
        }
        config = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(directory,config);
        Term term = new Term("id",String.valueOf(postId));
        Document document = wrapperDocument(p);
        try {
            indexWriter.updateDocument(term,document);
            indexWriter.commit();
            flag = true;
        }catch (Exception e){
            log.error(e.getMessage());
            indexWriter.rollback();
        }finally {
            indexWriter.close();
            indexWriter = null;
            config = null;
        }
        return flag;
    }

    @Override
    public List<Post> queryAllByKeyword(String keyword) throws ParseException, IOException {
        String[] columns = {"id","title","slug","summary","thumb","content","author"};
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(columns,analyzer);
        Query query = queryParser.parse(keyword);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        TopDocs search = indexSearcher.search(query,100);
        ScoreDoc[] scoreDocs =search.scoreDocs;
        List<Post> data = new ArrayList<>();
        for(ScoreDoc doc:scoreDocs){
            int i = doc.doc;
            Document document = indexSearcher.doc(i);
            data.add(wrapperPost(document));
        }
        return data;
    }

    @Override
    public Page<Post> findAll(String keyword, Pageable pageable) throws ParseException, IOException {
        String[] columns = {"id","title","slug","summary","thumb","content","author"};
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(columns,analyzer);
        Query query = queryParser.parse(keyword);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        TopDocs search = indexSearcher.search(query,100);
        ScoreDoc[] scoreDocs =search.scoreDocs;
        List<Post> data = new ArrayList<>();
        int offset = pageable.getPageNumber()+1;
        int size = pageable.getPageSize();
        int start = (offset-1)*size;
        int end = offset*size;
        int total = scoreDocs.length;
        if(start>total){
            return new PageImpl<>(Collections.emptyList(),pageable,scoreDocs.length);
        }
        if(end > total){
            end = total;
        }
        for (int i=start;i<end;i++){
            Document document = indexSearcher.doc(scoreDocs[i].doc);
            data.add(wrapperPost(document));
        }
        return new PageImpl<>(data,pageable,scoreDocs.length);
    }

    @Override
    public boolean resetIndex(){
        try {
            deleteAllIndex();
            int count = addIndexs();
            return count > 0;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return false;
    }

    private Document wrapperDocument(Post post){
        Document document = new Document();
        document.add(new TextField("id",String.valueOf(post.getId()), Field.Store.YES));
        document.add(new TextField("title",post.getTitle(),Field.Store.YES));
        document.add(new TextField("slug",post.getSlug(),Field.Store.YES));
        document.add(new TextField("summary",post.getSummary(),Field.Store.YES));
        document.add(new TextField("thumb",post.getThumb(),Field.Store.YES));
        document.add(new TextField("content",post.getHtml(),Field.Store.YES));
        document.add(new TextField("author",post.getAuthor(),Field.Store.YES));
        return document;
    }
    private Post wrapperPost(Document document){
        Post post = new Post();
        post.setId(Integer.parseInt(document.get("id")));
        post.setTitle(document.get("title"));
        post.setSlug(document.get("slug"));
        post.setSummary(document.get("summary"));
        post.setThumb(document.get("thumb"));
        post.setHtml(document.get("content"));
        post.setAuthor(document.get("author"));
        return post;
    }
}
