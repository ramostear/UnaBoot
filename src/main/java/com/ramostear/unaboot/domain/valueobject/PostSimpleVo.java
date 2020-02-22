package com.ramostear.unaboot.domain.valueobject;

import com.ramostear.unaboot.domain.dto.PostSimpleDto;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PostSimpleVo
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/23 0023 6:00
 * @Version since UnaBoot-1.0
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostSimpleVo extends PostSimpleDto implements Serializable {

    private List<Tag> tags;

    private Category category;

}
