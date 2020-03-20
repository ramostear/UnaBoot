package com.ramostear.unaboot.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @ClassName BlogNotFoundException
 * @Description 博客404异常类
 * @Author 树下魅狐
 * @Date 2020/3/18 0018 6:50
 * @Version since UnaBoot-1.0
 **/
public class BlogNotFoundException extends UnaBootException {

    public BlogNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
