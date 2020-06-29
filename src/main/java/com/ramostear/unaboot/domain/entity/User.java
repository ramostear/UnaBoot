package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.common.Authorized;
import com.ramostear.unaboot.common.Locked;
import com.ramostear.unaboot.common.State;
import com.ramostear.unaboot.domain.AbstractEntity;
import com.ramostear.unaboot.util.DateTimeUtils;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 5:06.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -7419853839002968103L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Lob
    @Type(type="text")
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "state")
    private Integer state = State.OPEN.getCode();

    @Column(name = "role")
    private String role;

    private Integer locked;

    @Override
    protected void prePersist() {
        Date now = DateTimeUtils.now();
        id = null;
        createTime = createTime==null?now:createTime;
        updateTime = now;
        if(StringUtils.isBlank(role)){
            role = Authorized.GUEST.getName();
        }
        if(locked == null){
            locked = Locked.NO.getCode();
        }
        if(StringUtils.isBlank(nickname)){
            nickname = "Unnamed";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, username, email, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ",role=" + role +
                ",locked=" + locked +
                '}';
    }
}
