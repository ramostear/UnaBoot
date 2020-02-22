package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.UnaBootPO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName PostTag
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 7:02
 * @Version since UnaBoot-1.0
 **/
@Entity
@Data
@Table(name = "post_tag")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostTag extends UnaBootPO implements Serializable {
    private static final long serialVersionUID = 1046489951865500363L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tid")
    private Integer tid;

    @Column(name = "pid")
    private Integer pid;

    @Override
    protected void prePersist() {
        super.prePersist();
        id = null;
    }
}
