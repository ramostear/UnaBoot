package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.PostStatus;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.domain.entity.Schedule;
import com.ramostear.unaboot.service.LuceneService;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.service.ScheduleService;
import com.ramostear.unaboot.service.TaskService;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 15:18.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService {

    private static CacheManager cacheManager = CacheManager.newInstance();

    @Autowired
    private PostService postService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private LuceneService luceneService;

    @Override
    public void publishPost(String params) {
        Integer postId = Integer.parseInt(params);
        Post post = postService.findById(postId);
        if(post != null && post.getStatus() == PostStatus.SCHEDULE){
            post.setStatus(PostStatus.ACTIVE);
            postService.update(post);
            Schedule schedule = scheduleService.findByParams(params);
            if(schedule != null){
                scheduleService.delete(schedule.getId());
            }
        }
    }

    @Override
    public void refreshIndex(String params) {
        luceneService.refresh();
    }

    @Override
    public void removeCache(String params) {
        String[] cacheNames = cacheManager.getCacheNames();
        if(cacheNames != null && cacheNames.length > 0){
            for(String name : cacheNames){
                Ehcache ehcache = cacheManager.getEhcache(name);
                if(name.equals("dayHits")){
                    clear(ehcache);
                }
                ehcache.removeAll();
            }
        }
    }

    private void clear(Ehcache ehcache){
        List<String> keys = (List<String>) ehcache.getKeys();
        List<Post> posts = new ArrayList<>();
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
                    posts.add(post);
                }
            });
            if(posts.size() > 0){
                postService.update(posts);
            }
        }
    }
}
