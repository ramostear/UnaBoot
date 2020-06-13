package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.Authorized;
import com.ramostear.unaboot.common.SortType;
import com.ramostear.unaboot.common.State;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.domain.vo.UserVo;
import com.ramostear.unaboot.exception.BadRequestException;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.UserService;
import com.ramostear.unaboot.util.AssertUtils;
import com.ramostear.unaboot.util.UnaBootUtils;
import com.ramostear.unaboot.web.UnaBootController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/29 0029 19:57.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Slf4j
@Controller
@RequestMapping("/admin/users")
public class UserController extends UnaBootController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String users(Model model){
        Page<User> data = userService.findAll(pageable("updateTime", SortType.DESC));
        model.addAttribute("data",data);
        return "/admin/user/list";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("roles", Authorized.values());
        return "/admin/user/create";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(User user){
       try {
           userService.create(user);
           return ok();
       }catch (Exception e){
           throw new BadRequestException(e.getMessage());
       }
    }

    @GetMapping("/{id:\\d+}")
    public String user(@PathVariable("id") Integer id,Model model){
        User user = userService.findById(id);
        model.addAttribute("user",user)
             .addAttribute("roles",Authorized.values());
        return "/admin/user/view";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}")
    public ResponseEntity<Object> user(@PathVariable("id")Integer id, UserVo userVo){
        return editUser(id,userVo);
    }

    @ResponseBody
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        User user = userService.findById(id);
        if(user == null){
            return bad();
        }
        try {
            userService.delete(user);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @ResponseBody
    @PutMapping("/{id:\\d+}/open")
    public ResponseEntity<Object> open(@PathVariable("id")Integer id){
        return editState(id,State.OPEN);
    }

    @ResponseBody
    @PutMapping("/{id:\\d+}/close")
    public ResponseEntity<Object> close(@PathVariable("id")Integer id){
        return editState(id,State.CLOSE);
    }

    @GetMapping("/{id:\\d+}/pwd")
    public String pwd(@PathVariable("id")Integer id,Model model){
        User user = userService.findById(id);
        model.addAttribute("user",user);
        return "/admin/user/pwd";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}/pwd")
    public ResponseEntity<Object> pwd(@PathVariable("id")Integer id,UserVo userVo){
        return editPassword(id,userVo);
    }

    @GetMapping("/profile")
    public String profile(Model model){
        model.addAttribute("user",currentUser());
        return "/admin/user/profile";
    }
    @ResponseBody
    @PostMapping("/profile")
    public ResponseEntity<Object> profile(UserVo userVo){
        return editUser(currentUser().getId(),userVo);
    }

    @GetMapping("/profile/pwd")
    public String profilePwd(Model model){
        model.addAttribute("user",currentUser());
        return "/admin/user/pwd";
    }

    @ResponseBody
    @PostMapping("/profile/pwd")
    public ResponseEntity<Object> profilePwd(UserVo userVo){
        return editPassword(currentUser().getId(),userVo);
    }

    @ResponseBody
    @GetMapping(value = "/{param}/notExist")
    public ResponseEntity<Object> notExist(@PathVariable("param")String param){
        if(StringUtils.isBlank(param)){
            return bad();
        }else if(userService.usernameNotExists(param)){
            return ok();
        }else{
            return bad();
        }
    }
    @ResponseBody
    @GetMapping(value = "/{param}/notExist",params = {"pattern=email"})
    public ResponseEntity<Object> emailNotExist(@PathVariable("param")String param){
        if(StringUtils.isBlank(param)){
            return bad();
        }else if(AssertUtils.isEmail(param) && userService.emailNotExists(param)){
            return ok();
        }else{
            return bad();
        }
    }
    /**
     * Edit user`s state where user`s id equals target id.
     * @param id            target id
     * @param state         state
     * @return              result
     */
    private ResponseEntity<Object> editState(Integer id, State state){
        User user = userService.findById(id);
        if(user == null){
            return bad();
        }
        if(state == State.OPEN){
            user.setState(State.OPEN.getCode());
        }else{
            user.setState(State.CLOSE.getCode());
        }
        try {
            userService.update(user);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    /**
     * Edit user`s base information
     * @param id            user`s identifier
     * @param userVo        user`s data
     * @return              ResponseEntity
     */
    private ResponseEntity<Object> editUser(Integer id,UserVo userVo){
        User user = userService.findById(id);
        if(user != null){
            BeanUtils.copyProperties(userVo,user, "username","password");
            userService.update(user);
            return ok();
        }
        return bad();
    }

    /**
     * Edit user`s username and password.
     * @param id        user`s identifier
     * @param userVo    user data
     * @return          ResponseEntity
     */
    private ResponseEntity<Object> editPassword(Integer id,UserVo userVo){
        User user = userService.findById(id);
        if(user == null || StringUtils.isBlank(userVo.getPassword())){
            return bad();
        }
        if(!user.getUsername().equals(userVo.getUsername())){
            user.setUsername(userVo.getUsername());
        }
        user.setPassword(UnaBootUtils.simpleHash(userVo.getPassword(),user.getUsername()));
        try {
            userService.update(user);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }
}
