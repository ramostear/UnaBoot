package com.ramostear.unaboot.service;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 15:16.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface TaskService {

    void publishPost(String params);

    void refreshIndex(String params);

    void removeCache(String params);
}
