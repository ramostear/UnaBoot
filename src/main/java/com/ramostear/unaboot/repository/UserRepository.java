package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/25 0025 6:29.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface UserRepository extends BaseRepository<User,Integer> {

    User findByUsername(String username);

    User findByEmail(String email);

    Long countByRole(String role);

    Page<User> findAllByRole(String role, Pageable pageable);
}
