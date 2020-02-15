package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.domain.UnaBootPO;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName Setting
 * @Description 系统配置对象
 * @Author 树下魅狐
 * @Date 2020/1/17 0017 4:14
 * @Version since UnaBoot-1.0
 **/
@Data
@Entity
@Builder
@Table(name="settings")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Setting extends UnaBootPO implements Serializable {
    private static final long serialVersionUID = -9169842407022800420L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "_key")
    private String key;

    @Lob
    @Type(type="text")
    @Column(name = "_value")
    private String value;

    @Override
    protected void prePersist() {
        super.prePersist();
        id = null;
        if(StringUtils.isBlank(key)){
            key = UnaBootConst.EMPTY_CHARACTER;
        }
        if(StringUtils.isBlank(value)){
            value = UnaBootConst.EMPTY_CHARACTER;
        }
    }

}
