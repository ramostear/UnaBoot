package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.domain.entity.Permit;
import com.ramostear.unaboot.domain.entity.RolePermit;
import com.ramostear.unaboot.domain.vo.PermitVo;
import com.ramostear.unaboot.repository.PermitRepository;
import com.ramostear.unaboot.repository.RolePermitRepository;
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

    @Autowired
    public PermitServiceImpl(PermitRepository permitRepository,RolePermitRepository rolePermitRepository) {
        super(permitRepository);
        this.permitRepository = permitRepository;
        this.rolePermitRepository = rolePermitRepository;
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
    public List<Permit> findAllByRoleId(Integer roleId) {
        List<RolePermit> rps = rolePermitRepository.findAllByRoleId(roleId);
        if(CollectionUtils.isEmpty(rps)){
            return Collections.emptyList();
        }
        return permitRepository.findAllById(rps.stream().map(RolePermit::getPermitId).collect(Collectors.toList()));
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
