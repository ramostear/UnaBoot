package com.ramostear.unaboot.domain.valueobject;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ArchiveVo
 * @Description 博客归档
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 16:44
 * @Version since UnaBoot-1.0
 **/
@Data
public class ArchiveVo implements Serializable {
    private static final long serialVersionUID = -8594192351118152587L;
    private String name;
    private Long counts;
}
