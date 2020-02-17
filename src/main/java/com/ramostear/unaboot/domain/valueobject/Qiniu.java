package com.ramostear.unaboot.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Qiniu
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/15 0015 23:17
 * @Version since UnaBoot-1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Qiniu {

    private String domain;

    private String secretKey;

    private String accessKey;

    private String bucket;

    private boolean enabled;

    private String zone;
}
