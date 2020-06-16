package com.ramostear.unaboot.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/29 0029 20:32.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    private String username;
    private String email;
    private String password;
    private String nickname;
    private String avatar;
    private String role;
}
