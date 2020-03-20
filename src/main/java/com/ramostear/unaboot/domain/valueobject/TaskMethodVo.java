package com.ramostear.unaboot.domain.valueobject;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName TaskMethodVo
 * @Description 定时任务方法名中英文对象
 * @Author 树下魅狐
 * @Date 2020/3/19 0019 3:59
 * @Version since UnaBoot-1.0
 **/
@Data
@Builder
public class TaskMethodVo {
    private String zhName;
    private String enName;
}
