package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.domain.UnaBootPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class User extends UnaBootPO implements Serializable {
    private static final long serialVersionUID = 5297791823938051661L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "role")
    private String role;

    @Column(name = "type",columnDefinition = "int default 0")
    private Integer type;

    @Override
    protected void prePersist() {
        super.prePersist();
        id = null;
        if(StringUtils.isBlank(role)){
            role = UnaBootConst.ROLE_ADMIN;
        }
    }
}
