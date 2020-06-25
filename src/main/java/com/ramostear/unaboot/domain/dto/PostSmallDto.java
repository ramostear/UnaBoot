package com.ramostear.unaboot.domain.dto;

import com.ramostear.unaboot.common.ExportConvert;
import com.ramostear.unaboot.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 1:33.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSmallDto implements ExportConvert<PostSmallDto, Post> {

    private Integer id;

    private String title;

    private String slug;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Long visits;

    private String thumb;

    private Integer style;

    private Integer userId;

}
