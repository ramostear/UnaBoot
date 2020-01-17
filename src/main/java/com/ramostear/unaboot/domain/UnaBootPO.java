package com.ramostear.unaboot.domain;

import com.ramostear.unaboot.common.util.DateTimeUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName UnaBootPO
 * @Description 公共的PO类
 * @Author 树下魅狐
 * @Date 2020/1/17 0017 4:15
 * @Version since UnaBoot-1.0
 **/
@MappedSuperclass
@Data
@ToString
@EqualsAndHashCode
public class UnaBootPO {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time",columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Date createTime;

    @Column(name = "update_time",columnDefinition = "timestamp")
    private Date updateTime;

    @PrePersist
    protected void prePersist(){
        Date current = DateTimeUtils.current();
        if(createTime == null) createTime = current;
        if(updateTime == null) updateTime = current;
    }

    @PreUpdate
    protected void preUpdate(){
        updateTime = DateTimeUtils.current();
    }
}
