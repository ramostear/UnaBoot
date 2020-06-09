package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.AbstractEntity;
import com.ramostear.unaboot.util.DateTimeUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 15:05.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@Entity
@ToString(callSuper = true)
@Table(name = "post_category")
@EqualsAndHashCode(callSuper = true)
public class PostCategory extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 6850565954566654844L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "category_id")
    private Integer categoryId;

    public PostCategory(){}

    public PostCategory(Integer postId,Integer categoryId){
        this.postId = postId;
        this.categoryId = categoryId;
    }

    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        createTime = createTime==null?now:createTime;
        updateTime = now;
        id = null;
    }
}
