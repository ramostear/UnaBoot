package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.ObjectType;
import com.ramostear.unaboot.domain.entity.Permit;
import com.ramostear.unaboot.domain.entity.RolePermit;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.domain.entity.UserRole;
import com.ramostear.unaboot.domain.vo.PermitVo;
import com.ramostear.unaboot.repository.PermitRepository;
import com.ramostear.unaboot.repository.RolePermitRepository;
import com.ramostear.unaboot.repository.UserRepository;
import com.ramostear.unaboot.repository.UserRoleRepository;
import com.ramostear.unaboot.service.PermitService;
import org.apache.shiro.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/30 0030 5:06.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Service("permitService")
public class PermitServiceImpl extends BaseServiceImpl<Permit,Integer> implements PermitService {

    private final PermitRepository permitRepository;
    private final RolePermitRepository rolePermitRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public PermitServiceImpl(PermitRepository permitRepository,RolePermitRepository rolePermitRepository,
                             UserRepository userRepository,UserRoleRepository userRoleRepository) {
        super(permitRepository);
        this.permitRepository = permitRepository;
        this.rolePermitRepository = rolePermitRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public PermitVo tree(Sort sort) {
        Assert.notNull(sort,"Sort data cannot be null.");
        PermitVo rootNode = initializeRootNode();
        List<Permit> permits = permitRepository.findAll(sort);
        if(!CollectionUtils.isEmpty(permits)){
            initializeTreeNodes(rootNode,permits);
        }
        return rootNode;
    }

    @Override
    public PermitVo tree(Integer identifier, ObjectType type) {
        PermitVo root = initializeRootNode();
        List<Permit> permits;
        if(type == ObjectType.ROLE){
            permits = this.findAllByRoleId(identifier);
        }else{
            permits = this.findAllByUserId(identifier);
        }
        if(!CollectionUtils.isEmpty(permits)){
            initializeTreeNodes(root,permits);
        }
        return root;
    }

    @Override
    public List<Permit> findAllByRoleId(Integer roleId) {
        List<RolePermit> rps = rolePermitRepository.findAllByRoleId(roleId);
        if(CollectionUtils.isEmpty(rps)){
            return Collections.emptyList();
        }
        return permitRepository.findAllById(rps.stream().map(RolePermit::getPermitId).collect(Collectors.toList()));
    }

    @Override
    public List<Permit> findAllByUserId(Integer userId) {
        //1.Get user`s data by user id.
        User user = userRepository.findById(userId).orElse(null);
        if(null == user){
            return Collections.emptyList();
        }
        //2.Get user-role relationship by user`id
        List<UserRole> urs = userRoleRepository.findAllByUserId(user.getId());
        if(org.apache.shiro.util.CollectionUtils.isEmpty(urs)){
            return Collections.emptyList();
        }
        //3.Get role-permit relationship by role`s id
        List<RolePermit> rps = rolePermitRepository.findAllByRoleIdIn(urs.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
        if(org.apache.shiro.util.CollectionUtils.isEmpty(rps)){
            return Collections.emptyList();
        }
        //4.Get user`a permit data and returned.
        return permitRepository.findAllById(rps.stream().map(RolePermit::getPermitId).distinct().collect(Collectors.toList()));
    }


    private PermitVo initializeRootNode(){
        PermitVo root = new PermitVo();
        root.setId(0);
        root.setName("权限列表");
        root.setChildren(new LinkedList<>());
        root.setPid(-1);
        return root;
    }

    private void initializeTreeNodes(PermitVo root, Collection<Permit> nodes){
        List<Permit> subNodes = nodes.stream()
                .filter(node-> Objects.equals(root.getId(),node.getPid()))
                .collect(Collectors.toList());
        subNodes.forEach(node-> root.getChildren().add(new PermitVo().convertFrom(node)));
        nodes.removeAll(subNodes);
        if(!CollectionUtils.isEmpty(root.getChildren())){
            root.getChildren().forEach(node->initializeTreeNodes(node,nodes));
        }
    }
}
