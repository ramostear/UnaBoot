package com.ramostear.unaboot.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 5:00.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@Builder
public class QiniuProperty {
    private String domain;
    private String secretKey;
    private String accessKey;
    private String bucket;
    private boolean enabled;
    private String zone;
}
