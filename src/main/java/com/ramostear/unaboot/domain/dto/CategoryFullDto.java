package com.ramostear.unaboot.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 13:56.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CategoryFullDto extends CategoryDto {
    private Long postSize;
}
