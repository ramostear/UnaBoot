package com.ramostear.unaboot.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CategoryInfoVo
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 7:42
 * @Version since UnaBoot-1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoVo {

    private Integer id;

    private String name;

    private String slug;

    private Long posts;
}
