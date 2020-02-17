package com.ramostear.unaboot.domain.dto;

import com.ramostear.unaboot.domain.dto.support.ObjectOutputConverter;
import com.ramostear.unaboot.domain.entity.Category;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName CategoryDto
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/14 0014 15:50
 * @Version since UnaBoot-1.0
 **/
@Data
public class CategoryDto implements ObjectOutputConverter<CategoryDto, Category> {

    private Integer id;

    private String name;

    private String slug;

    private Integer pid;

    private String pname;

    private Integer sortId;

    private String keywords;

    private String describe;

    private String theme;

    private Integer show;

    private Date createTime;
}
