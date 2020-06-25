package com.ramostear.unaboot.domain.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/12 0012 20:05.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
public class LoginParam {

    @NotBlank(message = "请输入您的登录账号")
    private String account;

    @NotBlank(message = "请输入您的登录密码")
    private String password;

    @NotBlank(message = "请填写验证码")
    private String captcha;
}
