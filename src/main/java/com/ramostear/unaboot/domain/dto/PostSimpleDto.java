package com.ramostear.unaboot.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 1:35.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostSimpleDto extends PostSmallDto {

    private String summary;

    private String tpl;

    private Boolean comment;

    private Boolean stick;

    private Boolean recommend;

    private Integer source;

    private String author;

    private Integer userId;

    private String url;

    private String keywords;


}
