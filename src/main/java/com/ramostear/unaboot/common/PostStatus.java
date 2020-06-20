package com.ramostear.unaboot.common;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 2:29.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface PostStatus {
    int DRAFT = -1; //草稿
    int WAIT = 0;   //等待审核
    int ACTIVE = 1;    //已发布
    int SCHEDULE = 2;//发布中
}
