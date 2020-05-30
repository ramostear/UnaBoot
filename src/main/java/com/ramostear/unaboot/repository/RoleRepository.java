package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.Role;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 21:06.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface RoleRepository extends BaseRepository<Role,Integer> {

    boolean existsByAlias(String alias);
}
