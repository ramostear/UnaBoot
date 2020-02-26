package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.common.util.CronUtils;
import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.domain.UnaBootPO;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UnaBootJob
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/16 0016 18:58
 * @Version since UnaBoot-1.0
 **/
@Data
@Entity
@Table(name = "unaboot_job")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class UnaBootJob extends UnaBootPO implements Serializable {

    private static final long serialVersionUID = 7272660722541616677L;

    @Id
    @Column(name = "job_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @Column(name = "bean_name")
    private String beanName;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "method_params")
    private String params;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "cron_time",columnDefinition = "timestamp")
    private Date cronTime;

    @Column(name = "job_state",columnDefinition = "int default 1")
    private Boolean jobState;

    @Column(name = "remark")
    private String remark;

    @Override
    protected void prePersist() {
        super.prePersist();
        jobId = null;
        if(cronTime == null){
            cronTime = DateTimeUtils.append(new Date(),1, TimeUnit.DAYS);
        }
        if(StringUtils.isEmpty(cronExpression)){
            cronExpression = CronUtils.getCron(cronTime);
        }
    }
}
