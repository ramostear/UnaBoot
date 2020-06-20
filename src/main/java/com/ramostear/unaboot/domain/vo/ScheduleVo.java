package com.ramostear.unaboot.domain.vo;

import com.ramostear.unaboot.util.DateTimeUtils;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/19 0019 6:15.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
public class ScheduleVo {
    private Integer type = 1;
    private Date publishDate = DateTimeUtils.append(new Date(),1, TimeUnit.DAYS);
}
