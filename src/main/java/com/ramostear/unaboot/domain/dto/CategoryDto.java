package com.ramostear.unaboot.domain.dto;

import com.ramostear.unaboot.common.ExportConvert;
import com.ramostear.unaboot.domain.entity.Category;
import lombok.Data;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 1:55.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
public class CategoryDto implements ExportConvert<CategoryDto, Category> {
    private Integer id;
    private String name;
    private String slug;
    private Integer pid;
    private Integer sortId;
    private String theme;
    private String thumb;
    private Integer navShow;
    private String postTheme;
}
