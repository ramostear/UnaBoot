package com.ramostear.unaboot.domain;

import com.ramostear.unaboot.util.DateTimeUtils;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 4:55.
 * The following is the description information about this file:</p>
 * <p>public abstract POJO class,which wraps basic attributes and methods.</p>
 */
@Data
@MappedSuperclass
public abstract class AbstractEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time",columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    protected Date createTime;

    @Column(name = "update_time",columnDefinition = "timestamp")
    protected Date updateTime;

    @PrePersist
    protected abstract void prePersist();

    @PreUpdate
    protected void preUpdate(){
        updateTime = DateTimeUtils.now();
    }
}
