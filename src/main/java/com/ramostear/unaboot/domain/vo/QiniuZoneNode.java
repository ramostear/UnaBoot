package com.ramostear.unaboot.domain.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 5:15.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Getter
@Setter
public class QiniuZoneNode {
    private String name;
    private String code;
    public QiniuZoneNode(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
