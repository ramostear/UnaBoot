package com.ramostear.unaboot.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 21:01.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Getter
@Setter
@Entity
@Table(name = "role_permit")
@NoArgsConstructor
public class RolePermit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "permit_id")
    private Integer permitId;

    public RolePermit(Integer roleId,Integer permitId){
        this.roleId = roleId;
        this.permitId = permitId;
    }
}
