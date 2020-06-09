package com.ramostear.unaboot.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 14:08.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySimpleVo {

    private Integer id;

    private String name;

    private String slug;

    private Long postSize;
}
