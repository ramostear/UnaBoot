package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.common.Authorized;
import com.ramostear.unaboot.domain.AbstractEntity;
import com.ramostear.unaboot.util.DateTimeUtils;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 5:18.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Entity
@Getter
@Setter
@Table(name = "role")
@NoArgsConstructor
public class Role extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = -5619364003282429604L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "alias")
    private String alias;

    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        createTime = createTime==null?now:createTime;
        updateTime = now;
        id = null;
        if(StringUtils.isBlank(name)){
            name = Authorized.GUEST.getName();
        }
        if(StringUtils.isEmpty(alias)){
            if(StringUtils.isNotBlank(name)){
                alias = Authorized.valueOf(name).getAlias();
            }
            alias = Authorized.GUEST.getAlias();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) &&
                Objects.equals(name, role.name) &&
                Objects.equals(alias,role.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
