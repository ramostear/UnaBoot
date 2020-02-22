package com.ramostear.unaboot.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @ClassName CategoryPostsDto
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 7:15
 * @Version since UnaBoot-1.0
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CategoryPostsDto extends CategoryDto {
    private Long posts;
}
