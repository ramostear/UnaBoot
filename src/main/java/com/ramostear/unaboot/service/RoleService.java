package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Role;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/30 0030 2:17.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface RoleService extends BaseService<Role,Integer>{

    boolean notExistByAlias(String alias);

    void updatePermits(Integer id, String permits);
}
