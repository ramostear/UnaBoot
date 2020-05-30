package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.Authorized;
import com.ramostear.unaboot.domain.entity.Role;
import com.ramostear.unaboot.domain.entity.RolePermit;
import com.ramostear.unaboot.repository.RolePermitRepository;
import com.ramostear.unaboot.repository.RoleRepository;
import com.ramostear.unaboot.repository.UserRoleRepository;
import com.ramostear.unaboot.service.RoleService;
import com.ramostear.unaboot.util.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/30 0030 2:18.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role,Integer> implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermitRepository rolePermitRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository,
                           RolePermitRepository rolePermitRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermitRepository = rolePermitRepository;
    }

    @Override
    @Transactional
    public Role update(Role role) {
        Assert.notNull(Authorized.valueOf(role.getName()),"Role type(name) does not exist.");
        Assert.isTrue(notExistByAlias(role.getAlias()),"The role already exists,please do not add it again.");
        role.setUpdateTime(DateTimeUtils.now());
        return roleRepository.save(role);
    }

    @Override
    public boolean notExistByAlias(String alias) {
        Assert.hasLength(alias,"alias must not be null.");
        return !roleRepository.existsByAlias(alias);
    }

    @Override
    @Transactional
    public void updatePermits(Integer id, String permits) {
        Role role = findById(id);
        if(StringUtils.isBlank(permits)){
            rolePermitRepository.deleteAllByRoleId(role.getId());
        }
        List<RolePermit> rps = Arrays.stream(StringUtils.split(permits, ","))
                .map(permitId-> new RolePermit(role.getId(),Integer.parseInt(permitId))).collect(Collectors.toList());
        rolePermitRepository.saveAll(rps);
    }

    @Override
    @Transactional
    public Role delete(Role role) {
        Assert.notNull(role,"The role data to be deleted cannot be empty.");
        //1.Disassociate role from users.
        userRoleRepository.deleteAllByRoleId(role.getId());
        //2.Disassociate role from permissions.
        rolePermitRepository.deleteAllByRoleId(role.getId());
        //3.Delete role data.
        roleRepository.delete(role);
        return role;
    }
}
