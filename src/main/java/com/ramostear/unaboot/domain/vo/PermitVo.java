package com.ramostear.unaboot.domain.vo;

import com.ramostear.unaboot.domain.dto.PermitDto;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/30 0030 5:16.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
public class PermitVo extends PermitDto {
    private boolean checked = false;
    private List<PermitVo> children = new LinkedList<>();
}
