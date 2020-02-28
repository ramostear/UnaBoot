package com.ramostear.unaboot.domain.param;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @ClassName LoginVo
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/28 0028 9:55
 * @Version since UnaBoot-1.0
 **/
@Data
public class LoginParam {

    @NotBlank(message = "登录账号不能为空")
    @Email(message = "请输入正确的邮箱地址")
    private String username;

    @NotBlank(message = "登录密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
