package com.ramostear.unaboot.domain.valueobject;

import lombok.Data;

/**
 * @ClassName VerifyCodeVo
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/28 0028 7:20
 * @Version since UnaBoot-1.0
 **/
@Data
public class VerifyCodeVo {

    private String code;
    private byte[] imgBytes;
    private long expire;
}
