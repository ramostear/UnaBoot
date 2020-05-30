package com.ramostear.unaboot.domain.dto;

import com.ramostear.unaboot.common.ExportConvert;
import com.ramostear.unaboot.domain.entity.Permit;
import lombok.Data;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/30 0030 5:14.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
public class PermitDto implements ExportConvert<PermitDto, Permit> {
    private Integer id;

    private String name;

    private String url;

    private String code;

    private Integer pid;
}
