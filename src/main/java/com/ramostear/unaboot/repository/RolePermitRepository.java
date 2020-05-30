package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.RolePermit;

import java.util.Collection;
import java.util.List;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 21:07.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface RolePermitRepository extends BaseRepository<RolePermit,Integer> {
    List<RolePermit> findAllByRoleIdIn(Collection<Integer> roleIds);
    List<RolePermit> findAllByRoleId(Integer roleId);
    void deleteAllByRoleId(Integer roleId);
}
