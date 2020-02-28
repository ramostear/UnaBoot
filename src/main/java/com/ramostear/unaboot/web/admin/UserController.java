package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.UnaBootConst;
import com.ramostear.unaboot.common.exception.UnaBootException;
import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.domain.param.UserParam;
import com.ramostear.unaboot.service.UserService;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 5:22
 * @Version since UnaBoot-1.0
 **/
@RequiresRoles(value = UnaBootConst.ROLE_ADMIN)
@Controller
@RequestMapping("/admin/user")
public class UserController extends UnaBootController {

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String index(Model model){
        Page<User> data = userService.findAll(pageByDesc("updateTime"));
        model.addAttribute("data",data);
        return "/admin/user/index";
    }

    @GetMapping("/create")
    public String create(){
        return "/admin/user/create";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(User user){
        user.setType(UnaBootConst.USER_NEW);
        user.setRole(UnaBootConst.ROLE_ADMIN);
        try {
            userService.create(user);
            return ok();
        }catch (UnaBootException e){
            return badRequest();
        }
    }

    @GetMapping("/{id:\\d+}")
    public String edit(@PathVariable("id")Integer id,Model model){
        User user = userService.findById(id);
        model.addAttribute("user",user);
        return "/admin/user/edit";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}")
    public ResponseEntity<Object> edit(@PathVariable("id")Integer id, UserParam param){
        User user = userService.findById(id);
        if(user == null || user.getType() == UnaBootConst.USER_DEFAULT){
            return badRequest();
        }
        try {
            if(!user.getUsername().equals(param.getUsername())){
                user.setUsername(param.getUsername());
            }
            user.setNickname(param.getNickname());
            user.setUpdateTime(DateTimeUtils.current());
            userService.update(user);

            userService.updatePassword(id,param.getPassword());
            return ok();
        }catch (UnaBootException e){
            return badRequest();
        }
    }

    @DeleteMapping("/{id:\\d+}")
    @ResponseBody
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        User user = userService.findById(id);
        if(user == null || user.getType() == UnaBootConst.USER_DEFAULT){
            return badRequest();
        }
        try {
            userService.delete(user);
            return ok();
        }catch (UnaBootException e){
            return badRequest();
        }
    }
}
