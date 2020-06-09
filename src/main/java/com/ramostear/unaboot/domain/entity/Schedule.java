package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.AbstractEntity;
import com.ramostear.unaboot.util.DateTimeUtils;
import com.ramostear.unaboot.util.UnaBootUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 15:25.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule")
public class Schedule extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1685077374823065566L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bean")
    private String bean;

    @Column(name = "method")
    private String method;

    @Column(name = "params")
    private String params;

    @Column(name = "cron_exp")
    private String cronExp;

    @Column(name = "state",columnDefinition = "int default 1")
    private boolean state;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "creator")
    private String creator;

    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        createTime = createTime==null?now:createTime;
        updateTime = now;
        id = null;
        if(StringUtils.isBlank(cronExp)){
            cronExp = UnaBootUtils.toCronExp(DateTimeUtils.append(createTime,1, TimeUnit.DAYS));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Schedule schedule = (Schedule) o;
        return state == schedule.state &&
                Objects.equals(id, schedule.id) &&
                Objects.equals(bean, schedule.bean) &&
                Objects.equals(method, schedule.method) &&
                Objects.equals(cronExp, schedule.cronExp) &&
                Objects.equals(introduce, schedule.introduce) &&
                Objects.equals(creator, schedule.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, bean, method, cronExp, state, introduce, creator);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", bean='" + bean + '\'' +
                ", method='" + method + '\'' +
                ", cronExp='" + cronExp + '\'' +
                ", state=" + state +
                ", introduce='" + introduce + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
