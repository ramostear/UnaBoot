package com.ramostear.unaboot.domain.vo;

import lombok.Data;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 1:27.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
public class QueryParam {

    private String key;

    private Integer status = Integer.MIN_VALUE;

    private Integer category = Integer.MIN_VALUE;

    private Integer style = Integer.MIN_VALUE;

    private Integer userId = Integer.MIN_VALUE;

}
