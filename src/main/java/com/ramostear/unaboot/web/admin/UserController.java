package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.Authorized;
import com.ramostear.unaboot.common.SortType;
import com.ramostear.unaboot.common.State;
import com.ramostear.unaboot.common.aspect.lang.LogType;
import com.ramostear.unaboot.common.aspect.lang.UnaLog;
import com.ramostear.unaboot.domain.entity.User;
import com.ramostear.unaboot.domain.vo.CategoryVo;
import com.ramostear.unaboot.domain.vo.UserVo;
import com.ramostear.unaboot.exception.BadRequestException;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.CategoryService;
import com.ramostear.unaboot.service.UserCategoryService;
import com.ramostear.unaboot.service.UserService;
import com.ramostear.unaboot.util.AssertUtils;
import com.ramostear.unaboot.util.UnaBootUtils;
import com.ramostear.unaboot.web.UnaBootController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    private final CategoryService categoryService;

    private final UserCategoryService userCategoryService;

    public UserController(UserService userService,CategoryService categoryService,
                          UserCategoryService userCategoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.userCategoryService = userCategoryService;
    }


    @UnaLog(title = "用户列表",type = LogType.LIST)
    @GetMapping("/")
    public String users(Model model){
        Page<User> data = userService.findAll(pageable("updateTime", SortType.DESC));
        model.addAttribute("data",data)
             .addAttribute("roles",Authorized.values())
             .addAttribute("all",userService.totalCount())
             .addAttribute("admin",userService.countByRole(Authorized.ADMIN.getName()));
        return "/admin/user/list";
    }

    @UnaLog(title = "用户角色",type = LogType.LIST)
    @GetMapping("/role/{role}")
    public String usersByRole(@PathVariable("role")String role, Model model){
        Page<User> data = userService.findAllByRole(role,pageable("updateTime", SortType.DESC));
        model.addAttribute("data",data)
                .addAttribute("roles",Authorized.values())
                .addAttribute("all",userService.totalCount())
                .addAttribute("admin",userService.countByRole(Authorized.ADMIN.getName()))
                .addAttribute("current",role);
        return "/admin/user/list";
    }

    @UnaLog(title = "新增用户",type = LogType.VIEW)
    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("roles", Authorized.values());
        return "/admin/user/create";
    }

    @UnaLog(title = "新增用户",type = LogType.INSERT)
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

    @UnaLog(title = "用户详情",type = LogType.VIEW)
    @GetMapping("/{id:\\d+}")
    public String user(@PathVariable("id") Integer id,Model model){
        User user = userService.findById(id);
        model.addAttribute("user",user)
             .addAttribute("roles",Authorized.values());
        return "/admin/user/view";
    }

    @UnaLog(title = "更新用户信息",type = LogType.UPDATE)
    @ResponseBody
    @PostMapping("/{id:\\d+}")
    public ResponseEntity<Object> user(@PathVariable("id")Integer id, UserVo userVo){
        return editUser(id,userVo);
    }

    @UnaLog(title = "删除用户",type = LogType.DELETE)
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

    @UnaLog(title = "激活用户",type = LogType.UPDATE)
    @ResponseBody
    @PutMapping("/{id:\\d+}/open")
    public ResponseEntity<Object> open(@PathVariable("id")Integer id){
        return editState(id,State.OPEN);
    }

    @UnaLog(title = "锁定用户",type = LogType.UPDATE)
    @ResponseBody
    @PutMapping("/{id:\\d+}/close")
    public ResponseEntity<Object> close(@PathVariable("id")Integer id){
        return editState(id,State.CLOSE);
    }


    @UnaLog(title = "修改密码",type = LogType.VIEW)
    @GetMapping("/{id:\\d+}/pwd")
    public String pwd(@PathVariable("id")Integer id,Model model){
        User user = userService.findById(id);
        model.addAttribute("user",user);
        return "/admin/user/pwd";
    }


    @UnaLog(title = "修改密码",type = LogType.UPDATE)
    @ResponseBody
    @PostMapping("/{id:\\d+}/pwd")
    public ResponseEntity<Object> pwd(@PathVariable("id")Integer id,UserVo userVo){
        return editPassword(id,userVo);
    }

    @UnaLog(title = "个人信息",type = LogType.VIEW)
    @GetMapping("/profile")
    public String profile(Model model){
        model.addAttribute("user",userService.findById(currentUser().getId()));
        return "/admin/user/profile";
    }

    @UnaLog(title = "更新个人信息",type = LogType.UPDATE)
    @ResponseBody
    @PostMapping("/profile")
    public ResponseEntity<Object> profile(UserVo userVo){
        return editUser(currentUser().getId(),userVo);
    }


    @UnaLog(title = "个人密码",type = LogType.VIEW)
    @GetMapping("/profile/pwd")
    public String profilePwd(Model model){
        model.addAttribute("user",currentUser());
        return "/admin/user/profile-pwd";
    }

    @UnaLog(title = "更新个人密码",type = LogType.UPDATE)
    @ResponseBody
    @PostMapping("/profile/pwd")
    public ResponseEntity<Object> profilePwd(UserVo userVo){
        return editPassword(currentUser().getId(),userVo);
    }


    @UnaLog(title = "用户名校验",type = LogType.UPDATE)
    @ResponseBody
    @GetMapping(value = "/validate/username")
    public boolean notExist(@RequestParam("username") String username){
        if(StringUtils.isBlank(username)){
            return false;
        }else if(userService.usernameNotExists(username)){
            return true;
        }else{
            return false;
        }
    }
    @UnaLog(title = "用户邮箱校验",type = LogType.UPDATE)
    @ResponseBody
    @GetMapping(value = "/validate/email")
    public boolean emailNotExist(@RequestParam("email")String email){
        if(StringUtils.isBlank(email)){
            return false;
        }else if(AssertUtils.isEmail(email) && userService.emailNotExists(email)){
            return true;
        }else{
            return false;
        }
    }

    @UnaLog(title = "用户栏目",type = LogType.VIEW)
    @GetMapping("/{id:\\d+}/categories")
    public String categories(@PathVariable("id")Integer id,Model model){
        model.addAttribute("userId",id);
        return "/admin/user/categories";
    }

    @UnaLog(title = "用户栏目数据",type = LogType.LIST)
    @GetMapping("/{id:\\d+}/categoryNodes")
    @ResponseBody
    public CategoryVo categories(@PathVariable("id")Integer id){
        CategoryVo node = categoryService.tree(Sort.by(Sort.Direction.ASC,"sortId"));
        List<Integer> categoryIds = userCategoryService.findAllCategoryByUserId(id);
        initializeTreeNodes(node,categoryIds);
        return node;
    }

    @UnaLog(title = "更新用户栏目数据",type = LogType.UPDATE)
    @PostMapping("/{id:\\d+}/categoryNodes")
    @ResponseBody
    public ResponseEntity<Object> categories(@PathVariable("id")Integer id,@RequestParam("categoryIds") String categoryIds){
        try {
            if(StringUtils.isBlank(categoryIds)){
                userCategoryService.mergeOrCreateIfAbsent(id,new ArrayList<>());
            }else{
                List<Integer> ids = Arrays.stream(categoryIds.split(","))
                        .map(Integer::parseInt).collect(Collectors.toList());
                userCategoryService.mergeOrCreateIfAbsent(id,ids);
            }
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    private void initializeTreeNodes(CategoryVo node, Collection<Integer> data){
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
