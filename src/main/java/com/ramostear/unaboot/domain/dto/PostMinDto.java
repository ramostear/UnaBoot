package com.ramostear.unaboot.domain.dto;

import com.ramostear.unaboot.common.ExportConvert;
import com.ramostear.unaboot.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 1:31.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostMinDto implements ExportConvert<PostMinDto, Post> {
    private Integer id;
    private String title;
    private String slug;
}
