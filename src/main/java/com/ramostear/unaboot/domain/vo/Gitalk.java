package com.ramostear.unaboot.domain.vo;

import lombok.Data;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 13:29.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
public class Gitalk {

    private boolean enabled = false;

    private String clientId;

    private String clientSecret;

    private String repo;

    private String owner;

    private String admin;
}
