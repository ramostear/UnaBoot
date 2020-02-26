package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.UnaBootPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Link
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 4:24
 * @Version since UnaBoot-1.0
 **/
@Data
@Entity
@Table(name = "links")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Link extends UnaBootPO implements Serializable {
    private static final long serialVersionUID = -410288001497605230L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name",columnDefinition = "varchar(64) not null")
    private String name;

    @Column(name = "domain",columnDefinition = "varchar(128) not null")
    private String domain;

    @Column(name = "sort_id",columnDefinition = "int default 0")
    private Integer sortId;

    @Override
    protected void prePersist() {
        super.prePersist();
        id = null;
    }
}
