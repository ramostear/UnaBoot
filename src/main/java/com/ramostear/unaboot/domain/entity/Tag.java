package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.UnaBootPO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Tag
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/22 0022 6:59
 * @Version since UnaBoot-1.0
 **/
@Entity
@Data
@Table(name = "tags")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Tag extends UnaBootPO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",columnDefinition = "varchar(32) not null")
    private String name;

    @Override
    protected void prePersist() {
        super.prePersist();
        id = null;
    }
}
