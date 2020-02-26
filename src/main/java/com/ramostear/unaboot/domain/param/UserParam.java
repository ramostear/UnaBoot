package com.ramostear.unaboot.domain.param;

import com.ramostear.unaboot.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName UserParam
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 5:39
 * @Version since UnaBoot-1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UserParam extends User {

    private String oldPassword;
}
