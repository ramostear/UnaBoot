package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.ObjectType;
import com.ramostear.unaboot.domain.vo.PermitVo;
import com.ramostear.unaboot.service.PermitService;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/1 0001 0:48.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
@RestController("/admin/permits")
public class PermitController extends UnaBootController {

    private final PermitService permitService;

    @Autowired
    PermitController(PermitService permitService){
        this.permitService = permitService;
    }

    @GetMapping("/")
    public PermitVo permits(){
        return permitService.tree(Sort.by(Sort.Direction.ASC,"id"));
    }

    @GetMapping("/role/{id:\\d+}")
    public PermitVo rolePermits(@PathVariable("id")Integer id){
        return permitService.tree(id, ObjectType.ROLE);
    }

    @GetMapping("/user/{id:\\d+}")
    public PermitVo userPermits(@PathVariable("id")Integer id){
        return permitService.tree(id, ObjectType.USER);
    }

}
