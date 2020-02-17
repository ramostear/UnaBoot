package com.ramostear.unaboot.domain.valueobject;

import lombok.Data;

/**
 * @ClassName Gitalk
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 4:40
 * @Version since UnaBoot-1.0
 **/
@Data
public class Gitalk {

    private boolean enabled = false;

    private String clientId;

    private String clientSecret;

    private String repo;

    private String owner;

    private String admin;
}
