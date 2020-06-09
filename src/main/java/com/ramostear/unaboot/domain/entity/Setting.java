package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.AbstractEntity;
import com.ramostear.unaboot.util.DateTimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 13:21.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Getter
@Setter
@Entity
@Table(name = "settings")
public class Setting extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 3066954912165612266L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "_key")
    private String key;

    @Lob
    @Type(type="text")
    @Column(name = "_value")
    private String value;

    public Setting() {
    }

    public Setting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        id = null;
        createTime = createTime==null?now:createTime;
        updateTime = now;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Setting setting = (Setting) o;
        return Objects.equals(id, setting.id) &&
                Objects.equals(key, setting.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, key);
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
