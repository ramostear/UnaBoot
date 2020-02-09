package com.ramostear.unaboot.repository;

import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.repository.support.UnaBootRepository;

public interface UserRepository extends UnaBootRepository<User,Integer> {

    /**
     *Query user detail information by username param
     * @param username  user`s username
     * @return  user entity
     */
    User findByUsername(String username);
}
