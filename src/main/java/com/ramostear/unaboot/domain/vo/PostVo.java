package com.ramostear.unaboot.domain.vo;

import com.ramostear.unaboot.domain.dto.PostDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.Set;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/4 0004 1:46.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostVo extends PostDto {
    private Set<Integer> tagIds;
    private Integer category;

    public String tagsToString(){
        if(CollectionUtils.isEmpty(tagIds)){
            return "";
        }
        String ids = "";
        Iterator<Integer> iterator = tagIds.iterator();
        while (iterator.hasNext()){
            ids += iterator.next()+",";
        }
        return ids.substring(0,ids.length()-1);
    }
}
