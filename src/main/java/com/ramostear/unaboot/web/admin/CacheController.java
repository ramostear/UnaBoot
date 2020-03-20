package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.service.LuceneService;
import com.ramostear.unaboot.service.PostService;
import com.ramostear.unaboot.web.UnaBootController;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CacheController
 * @Description 手动清理系统缓存数据
 * @Author 树下魅狐
 * @Date 2020/3/19 0019 3:09
 * @Version since UnaBoot-1.0
 **/
@RequiresRoles(value = UnaBootConst.ROLE_ADMIN)
@RestController
@RequestMapping("/admin/cache")
public class CacheController extends UnaBootController {

    @Autowired
    private LuceneService luceneService;
    @Autowired
    private PostService postService;
    private static CacheManager cacheManager = CacheManager.newInstance();

    @GetMapping("/refreshindex")
    public ResponseEntity<Object> rebuildSearchIndex(){
        if(luceneService.resetIndex()){
            return ok();
        }else{
            return badRequest();
        }
    }
    @GetMapping("/clearcache")
    public ResponseEntity<Object> clearCache(){
        String[] cacheNames = cacheManager.getCacheNames();
        if(cacheNames != null){
            for(String cacheName : cacheNames){
                Ehcache ehcache = cacheManager.getEhcache(cacheName);
                if(cacheName.equals("dayHits")){
                    flushDayHitsToDB(ehcache);
                }
                ehcache.removeAll();
            }
            return ok();
        }else{
            return badRequest();
        }
    }

    private void flushDayHitsToDB(Ehcache ehcache){
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
