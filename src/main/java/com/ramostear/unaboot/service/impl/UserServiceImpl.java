package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.*;
import com.ramostear.unaboot.repository.*;
import com.ramostear.unaboot.service.UserService;
import com.ramostear.unaboot.util.AssertUtils;
import com.ramostear.unaboot.util.DateTimeUtils;
import com.ramostear.unaboot.util.UnaBootUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.Assert;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 18:09.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User,Integer> implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermitRepository rolePermitRepository;
    private final PermitRepository permitRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
                           RolePermitRepository rolePermitRepository,PermitRepository permitRepository,
                           RoleRepository roleRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermitRepository = rolePermitRepository;
        this.permitRepository = permitRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public User create(User user) {
        Assert.notNull(user,"User data must not be null");
        Assert.hasLength(user.getUsername(),"username can not be empty.");
        Assert.hasLength(user.getPassword(),"password must not be null.");
        Assert.hasLength(user.getEmail(),"email must not be null.");
        Assert.isTrue(AssertUtils.isEmail(user.getEmail()),"email address is incorrect.");
        user.setPassword(UnaBootUtils.simpleHash(user.getPassword(),user.getUsername()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(User user) {
        Assert.notNull(user,"user data must not be null.");
        Assert.hasLength(user.getUsername(),"User`s username must not be null.");
        Assert.hasLength(user.getEmail(),"User`s email must not be null.");
        Assert.isTrue(AssertUtils.isEmail(user.getEmail()),"email address is incorrect.");
        user.setUpdateTime(DateTimeUtils.now());
        return userRepository.save(user);
    }

    @Override
    public boolean usernameNotExists(String username) {
        Assert.hasLength(username,"username must not be null.");
        User user = userRepository.findByUsername(username);
        return user == null;
    }

    @Override
    public boolean emailNotExists(String email) {
        Assert.hasLength(email,"email must not be null.");
        Assert.isTrue(AssertUtils.isEmail(email),"email address is incorrect.");
        User user = userRepository.findByEmail(email);
        return user == null;
    }

    @Override
    public List<Role> findAllRoleByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(null == user){
            return Collections.emptyList();
        }
        List<UserRole> urs = userRoleRepository.findAllByUserId(user.getId());
        if(CollectionUtils.isEmpty(urs)){
            return Collections.emptyList();
        }
        return roleRepository.findAllById(urs.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
    }

    @Override
    public List<Permit> findAllPermitByUserId(Integer userId) {
        //1.Get user`s data by user id.
        User user = userRepository.findById(userId).orElse(null);
        if(null == user){
            return Collections.emptyList();
        }
        //2.Get user-role relationship by user`id
        List<UserRole> urs = userRoleRepository.findAllByUserId(user.getId());
        if(CollectionUtils.isEmpty(urs)){
            return Collections.emptyList();
        }
        //3.Get role-permit relationship by role`s id
        List<RolePermit> rps = rolePermitRepository.findAllByRoleIdIn(urs.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
        if(CollectionUtils.isEmpty(rps)){
            return Collections.emptyList();
        }
        //4.Get user`a permit data and returned.
        return permitRepository.findAllById(rps.stream().map(RolePermit::getPermitId).distinct().collect(Collectors.toList()));
    }

    @Override
    public Optional<User> findByPrincipal(String principal) {
        if(StringUtils.isBlank(principal)){
            return Optional.empty();
        }
        User user;
        if(AssertUtils.isEmail(principal)){
            user = userRepository.findByEmail(principal);
        }else{
            user = userRepository.findByUsername(principal);
        }
        return (user == null)?Optional.empty():Optional.of(user);
    }
}
