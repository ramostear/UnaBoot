package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.SortType;
import com.ramostear.unaboot.common.aspect.lang.LogType;
import com.ramostear.unaboot.common.aspect.lang.UnaLog;
import com.ramostear.unaboot.domain.entity.Link;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.LinkService;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 19:48.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
@RequestMapping("/admin/links")
public class LinkController extends UnaBootController {

    private  final LinkService linkService;

    @Autowired
    public LinkController(LinkService linkService){
        this.linkService = linkService;
    }

    @UnaLog(title = "友链列表",type = LogType.LIST)
    @GetMapping("/")
    public String links(Model model){
        Page<Link> data = linkService.findAll(pageable("sortId", SortType.ASC,8));
        model.addAttribute("data",data);
        return "/admin/link/list";
    }

    @UnaLog(title = "新增友链",type = LogType.VIEW)
    @GetMapping("/create")
    public String create(){
        return "/admin/link/create";
    }

    @UnaLog(title = "新增友链",type = LogType.INSERT)
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(Link link){
        try {
            linkService.create(link);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @UnaLog(title = "查询友链",type = LogType.VIEW)
    @GetMapping("/{id:\\d+}")
    public String link(@PathVariable("id")Integer id,Model model){
        Link link = linkService.findByIdNullable(id);
        model.addAttribute("link",link);
        return "/admin/link/view";
    }

    @UnaLog(title = "修改友链",type = LogType.UPDATE)
    @ResponseBody
    @PostMapping("/{id:\\d+}")
    public ResponseEntity<Object> link(@PathVariable("id")Integer id,Link link){
        try {
            link.setId(id);
            linkService.update(link);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @UnaLog(title = "删除友链",type = LogType.DELETE)
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        try {
            linkService.delete(id);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

}
