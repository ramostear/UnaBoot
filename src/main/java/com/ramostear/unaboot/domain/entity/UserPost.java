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
 * <p>This java file was created by ramostear in 2020/6/3 0003 15:11.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@Entity
@Table(name = "user_post")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserPost extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = -3739787539021912820L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "post_id")
    private Integer postId;

    public UserPost(){}

    public UserPost(Integer userId,Integer postId){
        this.userId = userId;
        this.postId = postId;
    }

    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        createTime = createTime==null?now:createTime;
        updateTime = now;
        id = null;
    }
}
