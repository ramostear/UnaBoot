package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.AbstractEntity;
import com.ramostear.unaboot.util.DateTimeUtils;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 15:20.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@Entity
@Table(name = "link")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Link extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -7442860068413092499L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "sort_id",columnDefinition = "int default 0")
    private Integer sortId;

    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        createTime = createTime==null?now:createTime;
        updateTime = now;
        id = null;
    }
}
