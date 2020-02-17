package com.ramostear.unaboot.domain.valueobject;

import com.ramostear.unaboot.domain.dto.CategoryDto;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CategoryVo
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/14 0014 16:09
 * @Version since UnaBoot-1.0
 **/
@Data
public class CategoryVo extends CategoryDto {
    private List<CategoryVo> children;
}
