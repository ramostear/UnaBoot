package com.ramostear.unaboot.domain.vo;

import com.ramostear.unaboot.domain.dto.CategoryDto;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 1:57.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
public class CategoryVo extends CategoryDto {
    private List<CategoryVo> children = new LinkedList<>();
    private boolean checked = false;
}
