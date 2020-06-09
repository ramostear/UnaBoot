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
 * <p>This java file was created by ramostear in 2020/6/3 0003 15:08.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@Entity
@Table(name = "post_tag")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostTag extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 551803950310845425L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "tag_id")
    private Integer tagId;

    public PostTag(){}

    public PostTag(Integer postId,Integer tagId){
        this.postId = postId;
        this.tagId = tagId;
    }

    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        createTime = createTime==null?now:createTime;
        updateTime = now;
        id = null;
    }
}
