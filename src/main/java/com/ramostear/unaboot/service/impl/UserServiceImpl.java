package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.util.EncryptUtils;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.repository.UserRepository;
import com.ramostear.unaboot.service.UserService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service(value = "userService")
public class UserServiceImpl extends UnaBootServiceImpl<User,Integer> implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository repository) {
        super(repository);
        this.userRepository = repository;
    }

    @Override
    @Transactional
    public User create(User user) {
        Assert.notNull(user,"User data cannot be empty");
        Assert.hasLength(user.getUsername(),"Username can not be empty");
        Assert.hasLength(user.getPassword(),"User password can not be empty");
        user.setPassword(EncryptUtils.simpleHash(user.getPassword(),user.getUsername()));
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        Assert.notNull(username,"user`s name can not be empty");
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByCertificate(String certificate) {
        if(StringUtils.isBlank(certificate)){
            return Optional.empty();
        }
        User user = userRepository.findByUsername(certificate);
        if(user == null){
            return Optional.empty();
        }else{
            return Optional.of(user);
        }
    }

    @Override
    @Transactional
    public User updatePassword(Integer id, String originalPassword, String currentPassword) {
        Assert.notNull(id,"User`s ID can not be empty");
        Assert.notNull(originalPassword,"User`s original password can not be empty");
        Assert.notNull(currentPassword,"User`s new password can not be empty");
        Assert.isTrue(!currentPassword.equalsIgnoreCase(originalPassword),"The password cannot be the same twice");

        Optional<User> userOptional = userRepository.findById(id);
        Assert.isTrue(userOptional.isPresent(),"User data does not exist");

        User user = userOptional.get();
        String _validatePassword = EncryptUtils.simpleHash(originalPassword,user.getUsername());
        Assert.isTrue(_validatePassword.equals(user.getPassword()),"The original password is incorrect");
        user.setPassword(EncryptUtils.simpleHash(currentPassword,user.getUsername()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updatePassword(Integer id, String password) {
        Assert.notNull(id,"User`s ID can not be empty");
        Assert.notNull(password,"User`s new password can not be empty");
        Optional<User> userOptional = userRepository.findById(id);
        Assert.isTrue(userOptional.isPresent(),"User data does not exist");
        User user = userOptional.get();
        user.setPassword(EncryptUtils.simpleHash(password,user.getUsername()));
        return userRepository.save(user);
    }
}
