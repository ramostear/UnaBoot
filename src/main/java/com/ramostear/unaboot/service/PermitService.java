package com.ramostear.unaboot.service;

import com.ramostear.unaboot.common.ObjectType;
import com.ramostear.unaboot.domain.entity.Permit;
import com.ramostear.unaboot.domain.vo.PermitVo;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author :     ramostear/树下魅狐
 * @version :    Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/30 0030 5:05.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public interface PermitService extends BaseService<Permit,Integer> {

    PermitVo tree(@NonNull Sort sort);

    PermitVo tree(Integer identifier, ObjectType type);

    List<Permit> findAllByRoleId(Integer roleId);

    List<Permit> findAllByUserId(Integer userId);

}
