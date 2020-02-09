package com.ramostear.unaboot.domain.entity;

import com.ramostear.unaboot.domain.UnaBootPO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "theme")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Theme extends UnaBootPO implements Serializable {
    private static final long serialVersionUID = 2666521470555360932L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    public Theme(String name){
        this.name = name;
    }

    @Override
    protected void prePersist() {
        super.prePersist();
        id = null;
    }
}
