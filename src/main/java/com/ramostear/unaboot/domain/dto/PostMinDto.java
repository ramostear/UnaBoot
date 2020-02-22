package com.ramostear.unaboot.domain.dto;

import com.ramostear.unaboot.domain.dto.support.ObjectOutputConverter;
import com.ramostear.unaboot.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName PostMinDto
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 7:08
 * @Version since UnaBoot-1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostMinDto implements ObjectOutputConverter<PostMinDto, Post> {

    private Integer id;

    private String title;

    private String slug;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Long visits;
}
