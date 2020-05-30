package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.AbstractEntity;
import com.ramostear.unaboot.util.DateTimeUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 20:55.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Getter
@Setter
@Entity
@Table(name = "permit")
@NoArgsConstructor
public class Permit extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -2204773102583158402L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "code")
    private String code;

    @Column(name = "pid")
    private Integer pid;

    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        createTime = createTime==null?now:createTime;
        updateTime = now;
        id = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Permit permit = (Permit) o;
        return Objects.equals(id, permit.id) &&
                Objects.equals(name, permit.name) &&
                Objects.equals(url, permit.url) &&
                Objects.equals(code, permit.code) &&
                Objects.equals(pid, permit.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, url, code, pid);
    }

    @Override
    public String toString() {
        return "Permit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", code='" + code + '\'' +
                ", pid=" + pid +
                '}';
    }
}
