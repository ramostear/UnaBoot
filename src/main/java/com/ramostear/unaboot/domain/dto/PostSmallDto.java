package com.ramostear.unaboot.domain.dto;

import com.ramostear.unaboot.domain.dto.support.ObjectOutputConverter;
import com.ramostear.unaboot.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName PostSmallDto
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 7:07
 * @Version since UnaBoot-1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSmallDto implements ObjectOutputConverter<PostSmallDto, Post> {

    private Integer id;

    private String title;

    private String slug;
}
