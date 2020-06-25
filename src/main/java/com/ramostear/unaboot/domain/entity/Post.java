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
 * <p>This java file was created by ramostear in 2020/6/3 0003 14:40.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
@EqualsAndHashCode(callSuper = true)
public class Post extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = -9216372320061004572L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title",columnDefinition = "varchar(127) not null")
    private String title;       //文章标题

    @Column(name = "slug",columnDefinition = "varchar(127) not null")
    private String slug;    //文章的自定义访问路径

    @Column(name = "status",columnDefinition = "int default 0")
    private Integer status;     //文章状态，-1:草稿,0:等待发布,1：已发布

    @Column(name = "summary",columnDefinition = "varchar(255) default ''")
    private String summary;     //文章概要信息

    @Column(name = "keywords",columnDefinition = "varchar(127) default ''")
    private String keywords;    //文章的关键词，如果有多个，使用","分割

    @Column(name = "thumb",columnDefinition = "varchar(1024) default ''")
    private String thumb;       //文章封面图

    @Column(name = "visits",columnDefinition = "int default 0")
    private Long visits;        //文章浏览量

    @Column(name = "tpl",columnDefinition = "varchar(127) not null")
    private String tpl;     //文章显示的模板，默认是所属栏目设定的文章模板

    @Column(name = "stick",columnDefinition = "int default 0")
    private Boolean stick;  //置顶

    @Column(name = "recommend",columnDefinition = "int default 0")
    private Boolean recommend;//推荐

    @Column(name = "comment",columnDefinition = "int default 0")
    private Boolean comment;    //是否开启评论

    @Column(name = "style",columnDefinition = "int default 0")
    private Integer style;   //0：文章，1：文档

    @Column(name = "author",columnDefinition = "varchar(64) default ''")
    private String author;  //文章作者，默认是当前登录用户

    @Column(name = "user_id",columnDefinition = "int not null")
    private Integer userId; //用户ID

    @Column(name = "source",columnDefinition = "int default 0")
    private Integer source; //文章来源，0：原创，1：转载

    @Column(name = "url",columnDefinition = "varchar(255) default ''")
    private String url; //文章地址，如果是转载文章，填写此字段，原创文章不填写

    @Column(name = "content",columnDefinition = "longtext")
    private String content;     //文章正文

    @Column(name = "markdown",columnDefinition = "longtext")
    private String markdown;        //markdwon编辑器源码

    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        createTime = createTime==null?now:createTime;
        updateTime = now;
        id = null;
    }
}
