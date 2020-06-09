package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.service.TaskService;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public void publishPost(String params) {

    }

    @Override
    public void refreshIndex(String params) {

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
        List keys = ehcache.getKeys();
    }
}
