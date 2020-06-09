package com.ramostear.unaboot.domain.vo;

import com.ramostear.unaboot.domain.dto.PostSimpleDto;
import com.ramostear.unaboot.domain.entity.Category;
import com.ramostear.unaboot.domain.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 1:56.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostSimpleVo extends PostSimpleDto implements Serializable {
    private static final long serialVersionUID = -6153785914081596981L;
    private List<Tag> tags;

    private Category category;
}
