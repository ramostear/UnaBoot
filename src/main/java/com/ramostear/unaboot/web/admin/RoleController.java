package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.Authorized;
import com.ramostear.unaboot.domain.entity.Permit;
import com.ramostear.unaboot.domain.entity.Role;
import com.ramostear.unaboot.domain.vo.PermitVo;
import com.ramostear.unaboot.domain.vo.RoleVo;
import com.ramostear.unaboot.exception.NotFoundException;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.PermitService;
import com.ramostear.unaboot.service.RoleService;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/30 0030 2:31.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
@RequestMapping("/admin/roles")
public class RoleController extends UnaBootController {
    private final RoleService roleService;
    private final PermitService permitService;

    @Autowired
    RoleController(RoleService roleService,PermitService permitService){
        this.roleService = roleService;
        this.permitService = permitService;
    }

    @GetMapping("/")
    public String roles(Model model){
        Page<Role> data = roleService.findAll(pageable("createTime"));
        model.addAttribute("data",data);
        return "/admin/role/list";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("types", Authorized.values());
        return "/admin/role/create";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(Role role){
        try {
            roleService.create(role);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @GetMapping("/{id:\\d+}")
    public String role(@PathVariable("id")Integer id,Model model){
        Role role = roleService.findByIdNullable(id);
        model.addAttribute("role",role);
        model.addAttribute("types",Authorized.values());
        return "/admin/role/view";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}")
    public ResponseEntity<Object> role(@PathVariable("id")Integer id, RoleVo roleVo){
        try {
            Role role = roleService.findByIdNonNull(id);
            BeanUtils.copyProperties(roleVo,role);
            roleService.update(role);
            return ok();
        }catch (NotFoundException e){
            return bad();
        }
    }

    @ResponseBody
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        try {
            Role role = roleService.findById(id);
            roleService.delete(role);
            return ok();
        }catch (NotFoundException e){
            return bad();
        }
    }

    @GetMapping("/{id:\\d+}/permits")
    public String permits(@PathVariable("id")Integer id,Model model){
        List<Permit> permits = permitService.findAllByRoleId(id);
        PermitVo permitNodes = permitService.tree(Sort.by(Sort.Direction.ASC,"id"));
        if(!CollectionUtils.isEmpty(permits)){
            initializeTreeNodes(permitNodes,permits.stream().map(Permit::getId).collect(Collectors.toList()));
        }
        model.addAttribute("permitNodes",permitNodes);
        return "/admin/role/permits";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}/permits")
    public ResponseEntity<Object> permits(@PathVariable("id")Integer id,String permits){
        try {
            roleService.updatePermits(id,permits);
            return ok();
        }catch (NotFoundException e){
            return bad();
        }
    }

    private void initializeTreeNodes(PermitVo node, Collection<Integer> data){
        if(!CollectionUtils.isEmpty(data)){
            int checkedId = -1;
            if(data.contains(node.getId())){
                node.setChecked(true);
                checkedId = node.getId();
            }
            if(checkedId > -1){
                data.remove(checkedId);
            }
            if(!CollectionUtils.isEmpty(node.getChildren())){
                node.getChildren().forEach(item->initializeTreeNodes(item,data));
            }
        }
    }
}
