package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.UnaBootPO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName PostCategory
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 6:56
 * @Version since UnaBoot-1.0
 **/
@Data
@Entity
@Table(name = "post_category")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostCategory extends UnaBootPO implements Serializable {
    private static final long serialVersionUID = 280706548458376355L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cid")
    private Integer cid;

    @Column(name = "pid")
    private Integer pid;

    @Override
    protected void prePersist() {
        super.prePersist();
        id = null;
    }
}
