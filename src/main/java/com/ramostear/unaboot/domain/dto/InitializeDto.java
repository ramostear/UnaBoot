package com.ramostear.unaboot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/12 0012 23:30.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitializeDto {
    private String host;
    private String port;
    private String db;
    private String username;
    private String password;
    private String title;
    private String domain;
    private String adminAccount;
    private String adminPassword;
    private String adminEmail;
}
