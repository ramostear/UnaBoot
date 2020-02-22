package com.ramostear.unaboot.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @ClassName PostSimpleDto
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 7:10
 * @Version since UnaBoot-1.0
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostSimpleDto extends PostMinDto {

    private String summary;

    private String thumb;

    private Long likes = 0L;

    private String theme;

    private Boolean comment;

    private Boolean stick;

    private Boolean recommend;

    private Boolean original;

    private String author;

    private String keywords;

    private String projectUrl;
}
