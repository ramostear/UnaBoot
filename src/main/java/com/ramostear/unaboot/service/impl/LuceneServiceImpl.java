package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.service.LuceneService;
import com.ramostear.unaboot.service.PostService;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/23 0023 20:15.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("luceneService")
public class LuceneServiceImpl  implements LuceneService {

    @Autowired
    private PostService postService;

    private static StandardAnalyzer analyzer;

    private static Directory directory;

    private IndexWriterConfig config = null;

    private IndexWriter writer = null;

    static {
        analyzer = new StandardAnalyzer();
        try {
            File file = new File(Constants.UNABOOT_STORAGE_DIR+"lucene");
            if(!file.exists()){
                file.mkdirs();
            }
            directory = FSDirectory.open(Paths.get(Constants.UNABOOT_STORAGE_DIR+"lucene"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int storage() throws IOException {
        int count = 0;
        List<Post> data = postService.findAllByStatusIsPublished();
        if(!CollectionUtils.isEmpty(data)){
            ArrayList<Document> documents = new ArrayList<>();
            data.forEach(item-> documents.add(wrapDocument(item)));
            config = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(directory,config);
            writer.addDocuments(documents);
        }
        try {
            writer.commit();
            count = data.size();
        }catch (Exception e){
            e.printStackTrace();
            writer.rollback();
        }finally {
            writer.close();
            writer = null;
            config = null;
        }
        return count;
    }

    @Override
    public boolean clear() throws IOException {
        boolean isOk = false;
        config = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(directory,config);
        try {
            writer.deleteAll();
            writer.commit();
            isOk = true;
        }catch (Exception e){
            e.printStackTrace();
            writer.rollback();
        }finally {
            writer.close();
            writer = null;
            config = null;
        }
        return isOk;
    }

    @Override
    public Page<Post> findAll(String keyword, Pageable pageable) throws ParseException, IOException {
        String[] columns = {"id","title","slug","summary","thumb","content","author"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(columns,analyzer);
        Query query = parser.parse(keyword);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(query,1000);
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        int offset = pageable.getPageNumber()+1;
        int size = pageable.getPageSize();
        int start = (offset-1)*size;
        int end = offset*size;
        int total = scoreDocs.length;
        if(start > total){
            return new PageImpl<>(Collections.emptyList(),pageable,scoreDocs.length);
        }
        if(end > total){
            end = total;
        }
        List<Post> data = new ArrayList<>();
        for(int i=start;i<end;i++){
            Document document = searcher.doc(scoreDocs[i].doc);
            data.add(wrapPost(document));
        }
        return new PageImpl<>(data,pageable,scoreDocs.length);
    }

    @Override
    public boolean refresh() {
        try {
            clear();
            int count = storage();
            return count > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    private Document wrapDocument(Post post){
        Document document = new Document();
        document.add(new TextField("id",String.valueOf(post.getId()), Field.Store.YES));
        document.add(new TextField("title",String.valueOf(post.getTitle()), Field.Store.YES));
        document.add(new TextField("slug",String.valueOf(post.getSlug()), Field.Store.YES));
        document.add(new TextField("summary",String.valueOf(post.getSummary()), Field.Store.YES));
        document.add(new TextField("thumb",String.valueOf(post.getThumb()), Field.Store.YES));
        document.add(new TextField("content",String.valueOf(post.getContent()), Field.Store.YES));
        document.add(new TextField("author",String.valueOf(post.getAuthor()), Field.Store.YES));
        return document;
    }

    private Post wrapPost(Document document){
        Post post = new Post();
        post.setId(Integer.parseInt(document.get("id")));
        post.setTitle(document.get("title"));
        post.setSlug(document.get("slug"));
        post.setSummary(document.get("summary"));
        post.setThumb(document.get("thumb"));
        post.setContent(document.get("content"));
        post.setAuthor(document.get("author"));
        return post;
    }
}
