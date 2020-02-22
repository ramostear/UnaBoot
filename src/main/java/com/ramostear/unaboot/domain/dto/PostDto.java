package com.ramostear.unaboot.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @ClassName PostDto
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 7:13
 * @Version since UnaBoot-1.0
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostDto extends PostSimpleDto {

    private String html;

    private String markdown;
}
