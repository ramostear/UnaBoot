package com.ramostear.unaboot.domain.valueobject;

import com.ramostear.unaboot.domain.dto.PostDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName PostVo
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/23 0023 5:53
 * @Version since UnaBoot-1.0
 **/
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostVo extends PostDto {

    private Set<Integer> tagIds;

    private Integer category;

    public String convertTagIds(){
        if(CollectionUtils.isEmpty(tagIds)){
            return "";
        }
        String val = "";
        Iterator<Integer> iterator = tagIds.iterator();
        while (iterator.hasNext()){
            val += iterator.next();
        }
        return val.substring(0,val.length()-1);
    }

}
