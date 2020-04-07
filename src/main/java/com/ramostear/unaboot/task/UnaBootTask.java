package com.ramostear.unaboot.task;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.UnaBootJob;
import com.ramostear.unaboot.service.JobService;
import com.ramostear.unaboot.service.LuceneService;
import com.ramostear.unaboot.service.PostService;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UnaBootTask
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 19:48
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service("unaBootTask")
public class UnaBootTask {

    @Autowired
    private PostService postService;
    @Autowired
    private JobService jobService;
    @Autowired
    private LuceneService luceneService;
    private static CacheManager cacheManager = CacheManager.newInstance();

    /**
     * 定时发布文章
     * @param params    文章序号
     */
    public void publishPostTask(String params){
        Integer postId = Integer.valueOf(params);
        log.info("task params:{}",postId);
        Post post = postService.findById(postId);
        if(post != null && post.getStatus() == UnaBootConst.WAIT){
            post.setStatus(UnaBootConst.ACTIVE);
            postService.update(post);
            UnaBootJob job = jobService.findByParam(params);
            if(job != null){
                jobService.onlyRemoveJob(job);
            }
        }
    }

    /**
     * 重新构建全文检索数据索引
     */
    public void rebuildSearchIndex(){
        luceneService.resetIndex();
    }

    /**
     * 刷新系统缓存
     */
    public void clearCache(){
        String[] cacheNames = cacheManager.getCacheNames();
        if(cacheNames != null){
            for(String cacheName : cacheNames){
                Ehcache ehcache = cacheManager.getEhcache(cacheName);
                if(cacheName.equals("dayHits")){
                    clearCache(ehcache);
                }
                ehcache.removeAll();
            }
        }
    }

    private void clearCache(Ehcache ehcache){
        List<String> keys = (List<String>)ehcache.getKeys();
        List<Post> cachePosts = new ArrayList<>();
        if(!CollectionUtils.isEmpty(keys)){
            keys.forEach(key->{
                Element element = ehcache.get(key);
                Integer postId = Integer.parseInt(key.split("_")[0]);
                long count = 0;
                if(element != null){
                    count = (Long) element.getObjectValue();
                }
                Post post = postService.findById(postId);
                if(post != null){
                    post.setVisits(post.getVisits()+count);
                    cachePosts.add(post);
                }

            });
        }
        if(cachePosts.size() > 0){
            postService.updateInBatch(cachePosts);
        }
    }
}
