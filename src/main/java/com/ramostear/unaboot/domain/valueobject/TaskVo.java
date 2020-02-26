package com.ramostear.unaboot.domain.valueobject;

import com.ramostear.unaboot.common.util.DateTimeUtils;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName TaskVo
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/26 0026 8:56
 * @Version since UnaBoot-1.0
 **/
@Data
public class TaskVo {
    private Integer type = 1;
    private Date publishDate = DateTimeUtils.append(new Date(),1, TimeUnit.DAYS);
}
