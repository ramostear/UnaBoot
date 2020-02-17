package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.UnaBootPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Category
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/14 0014 15:30
 * @Version since UnaBoot-1.0
 **/
@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class Category extends UnaBootPO implements Serializable {
    private static final long serialVersionUID = -8774176816206473278L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "pid")
    private Integer pid;

    @Column(name = "sort_id")
    private Integer sortId;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "_describe")
    private String describe;

    @Column(name = "theme")
    private String theme;

    @Column(name = "is_show",columnDefinition = "int default 0")
    private Integer show;

    @Override
    protected void prePersist() {
        super.prePersist();
        id = null;
        if(pid == null || pid < 0){
            pid = 0;
        }
    }
}
