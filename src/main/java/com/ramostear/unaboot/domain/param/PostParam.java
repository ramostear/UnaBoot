package com.ramostear.unaboot.domain.param;

import com.ramostear.unaboot.common.ImportConvert;
import com.ramostear.unaboot.domain.entity.Post;
import com.ramostear.unaboot.util.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/19 0019 5:43.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostParam implements ImportConvert<Post> {

    @Size(max = 80,min = 2,message = "标题长度必须在{min}到{max}之间")
    private String title;       //文章标题

    @Size(max = 80,min = 2,message = "Slug长度必须在{min}到{max}之间")
    private String slug;    //文章的自定义访问路径

    private Integer status = -1;     //文章状态，-1:草稿,0:等待发布,1：已发布

    @NotBlank(message = "文章摘要不能为空")
    @Size(max = 128,message = "摘要长度不能超过{max}")
    private String summary;     //文章概要信息

    private String keywords;    //文章的关键词，如果有多个，使用","分割

    private String thumb;       //文章封面图

    private Long visits = 0L;        //文章浏览量

    private String tpl;     //文章显示的模板，默认是所属栏目设定的文章模板

    private Boolean stick  = false;  //置顶

    private Boolean recommend = false;//推荐

    private Boolean comment = false;    //是否开启评论

    private Integer style = 0;   //0：文章，1：文档

    private String author;  //文章作者，默认是当前登录用户

    private Integer source = 0; //文章来源，0：原创，1：转载

    private String url; //文章地址，如果是转载文章，填写此字段，原创文章不填写

    @NotBlank(message = "文章内容不能为空")
    private String content;     //文章正文

    private String markdown;        //markdwon编辑器源码

    private String tagIds;

    private Integer categoryId;

    private Date updateTime;

    public Set<Integer> tags(){
        if(StringUtils.isBlank(tagIds)){
            return Collections.emptySet();
        }else{
            return  Arrays.stream(tagIds.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Post convertTo(){
        updateTime = DateTimeUtils.now();
        return ImportConvert.super.convertTo();
    }
}
