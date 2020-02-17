package com.ramostear.unaboot.domain.valueobject;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName QiniuZone
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/15 0015 23:16
 * @Version since UnaBoot-1.0
 **/
@Data
@Builder
public class QiniuZone {

    private String name;

    private String code;
}
