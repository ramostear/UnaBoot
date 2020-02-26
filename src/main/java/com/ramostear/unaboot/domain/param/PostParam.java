package com.ramostear.unaboot.domain.param;

import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.common.util.RandomUtils;
import com.ramostear.unaboot.domain.dto.support.ObjectInputConverter;
import com.ramostear.unaboot.domain.entity.Post;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class PostParam  implements ObjectInputConverter<Post> {

    @Size(max = 64,min = 8,message = "Slug长度必须在{min}到{max}之间")
    private String slug;

    @NotBlank(message = "文章标题不能为空")
    @Size(max = 64,min = 8,message = "标题长度必须在{min}到{max}之间")
    private String title;

    private Integer status = 0;

    @NotBlank(message = "文章摘要不能为空")
    @Size(max = 128,message = "摘要长度不能超过{max}")
    private String summary;

    private String thumb;

    private Long visits = 0L;

    private Long likes = 0L;

    @NotBlank(message = "渲染模板不能为空")
    private String theme;

    private Boolean stick = false;//置顶

    private Boolean recommend = false; //推荐

    private Boolean comment = false; //评论

    @NotBlank(message = "文章作者不能为空")
    private String author;

    private String source;

    private Boolean original = true;

    @NotBlank(message = "请填写文章关键词")
    private String keywords;

    @NotBlank(message = "文章内容不能为空")
    private String markdown;

    private String html;

    private String projectUrl;

    private Date createTime;

    private String tags;

    @NotNull(message = "文章栏目不能为空")
    private Integer category;

    public Set<Integer> tagArray(){
        if(StringUtils.isBlank(tags)){
            return Collections.emptySet();
        }
        return Arrays.stream(tags.split(",")).map(tag->Integer.valueOf(tag)).collect(Collectors.toSet());
    }

    @Override
    public Post convertTo() {
        if(StringUtils.isBlank(slug)){
            slug = initSlug();
        }
        createTime = DateTimeUtils.current();
        return ObjectInputConverter.super.convertTo();
    }

    private String initSlug(){
        return new SimpleDateFormat("yyyy/MM/dd").format(DateTimeUtils.current())
                +"/"+RandomUtils.string(8)+".html";
    }

}
