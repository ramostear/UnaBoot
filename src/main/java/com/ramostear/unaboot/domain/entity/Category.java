package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.AbstractEntity;
import com.ramostear.unaboot.util.DateTimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 1:23.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 5522884605343148609L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name = "untitled";

    @Column(name = "slug")
    private String slug;//Category url name

    @Column(name = "pid")
    private Integer pid = 0;

    @Column(name = "sort_id")
    private Integer sortId;

    @Column(name = "theme")
    private String theme;

    @Column(name = "thumb")
    private String thumb;

    @Column(name = "nav_show")
    private Integer navShow = 0;

    @Column(name = "post_theme")
    private String postTheme;


    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        createTime = createTime==null?now:createTime;
        updateTime = now;
        id = null;
        if(StringUtils.isBlank(theme)){
            theme = "category";
        }
        if(StringUtils.isBlank(postTheme)){
            postTheme = "post";
        }

    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", pid=" + pid +
                ", sortId=" + sortId +
                ", theme='" + theme + '\'' +
                ", thumb='" + thumb + '\'' +
                ", navShow=" + navShow +
                ", postTheme='" + postTheme + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(name, category.name) &&
                Objects.equals(slug, category.slug) &&
                Objects.equals(pid, category.pid) &&
                Objects.equals(sortId, category.sortId) &&
                Objects.equals(theme, category.theme) &&
                Objects.equals(thumb, category.thumb) &&
                Objects.equals(navShow, category.navShow) &&
                Objects.equals(postTheme, category.postTheme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, slug, pid, sortId, theme, thumb, navShow, postTheme);
    }
}
