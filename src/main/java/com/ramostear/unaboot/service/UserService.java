package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 18:07.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface UserService extends BaseService<User,Integer>{
    /**
     * Determine if the username already exists
     * @param username      username
     * @return              boolean
     */
    boolean usernameNotExists(String username);

    /**
     * Determine if the email already exists.
     * @param email         email address
     * @return              boolean
     */
    boolean emailNotExists(String email);


    Optional<User> findByPrincipal(String principal);

    Long countByRole(String role);

    Page<User> findAllByRole(String role, Pageable pageable);
}
