package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.UnaBootPO;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Post
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/21 0021 18:37
 * @Version since UnaBoot-1.0
 **/
@Data
@Entity
@ToString
@Table(name="posts")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Post extends UnaBootPO implements Serializable {
    private static final long serialVersionUID = -590449818824582259L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "slug",columnDefinition = "varchar(128) not null")
    private String slug;

    @Column(name = "title",columnDefinition = "varchar(128) not null")
    private String title;

    @Column(name = "status",columnDefinition = "int default 0")
    private Integer status;

    @Column(name = "summary",columnDefinition = "varchar(256) default ''")
    private String summary;

    @Column(name = "thumb",columnDefinition = "varchar(1024) default ''")
    private String thumb;

    @Column(name = "visits",columnDefinition = "int default 0")
    private Long visits;

    @Column(name = "likes",columnDefinition = "int default 0")
    private Long likes;

    @Column(name = "theme",columnDefinition = "varchar(64) not null")
    private String theme;

    @Column(name = "stick",columnDefinition = "int default 0")
    private Boolean stick;//置顶

    @Column(name = "recommend",columnDefinition = "int default 0")
    private Boolean recommend; //推荐

    @Column(name = "comment",columnDefinition = "int default 0")
    private Boolean comment; //评论

    @Column(name = "style",columnDefinition = "int default 0")
    private Integer style;//文章类型

    @Column(name = "author",columnDefinition = "varchar(32) default ''")
    private String author;

    @Column(name = "source",columnDefinition = "varchar(64) default ''")
    private String source;

    @Column(name = "original",columnDefinition = "int default 1")
    private Boolean original;

    @Column(name = "keywords",columnDefinition = "varchar(128) default ''")
    private String keywords;

    @Column(name = "markdown",columnDefinition = "longtext")
    private String markdown;

    @Column(name = "html",columnDefinition = "longtext")
    private String html;

    @Column(name = "project_url",columnDefinition = "varchar(512) default ''")
    private String projectUrl;

    @Override
    protected void prePersist() {
        super.prePersist();
        id = null;
        if(status == null){
            status = 0;
        }
        if(StringUtils.isBlank(summary)){
            summary = "";
        }
        if(StringUtils.isEmpty(thumb)){
            thumb = "";
        }
        if(StringUtils.isEmpty(theme)){
            theme = "";
        }
        if(stick == null){
            stick = false;
        }
        if(recommend == null){
            recommend = false;
        }
        if(comment == null){
            comment = false;
        }
        if(visits == null || visits < 0){
            visits = 0L;
        }
        if(likes == null || likes <0){
            likes = 0L;
        }
        if(StringUtils.isBlank(projectUrl)){
            projectUrl = "";
        }
    }
}
