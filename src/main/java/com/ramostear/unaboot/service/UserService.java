package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.service.base.UnaBootService;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface UserService extends UnaBootService<User,Integer> {

    /**
     * Insert new user data into the database
     * @param user  user data
     * @return new user data
     */
    User create(User user);

    /**
     * Query user data by user name;
     * @param username  user name parameter
     * @return  user data
     */
    User findByUsername(String username);

    /**
     * Retrieve user data based on credential
     * @param certificate user credential
     * @return  Optional user data results
     */
    Optional<User> findByCertificate(@NonNull String certificate);

    /**
     * Modify user password, and provide user ID, original password and new password
     * @param id                    user identity
     * @param originalPassword      original password
     * @param currentPassword       new password
     * @return  user entity
     */
    User updatePassword(Integer id,String originalPassword,String currentPassword);
}
