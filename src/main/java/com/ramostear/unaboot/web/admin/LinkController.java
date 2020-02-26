package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.exception.UnaBootException;
import com.ramostear.unaboot.domain.entity.Link;
import com.ramostear.unaboot.service.LinkService;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName LinkController
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/27 0027 4:32
 * @Version since UnaBoot-1.0
 **/
@Controller
@RequestMapping("/admin/link")
public class LinkController extends UnaBootController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/index")
    public String index(Model model){
        Page<Link> data = linkService.findAll(page());
        model.addAttribute("data",data);
        return "/admin/link/index";
    }

    @GetMapping("/create")
    public String create(){
        return "/admin/link/create";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(Link link){
        try {
            linkService.create(link);
            return ok();
        }catch (UnaBootException e){
            return badRequest();
        }
    }

    @GetMapping("/{id:\\d+}")
    public String update(@PathVariable("id")Integer id,Model model){
        Link link = linkService.findById(id);
        model.addAttribute("link",link);
        return "/admin/link/edit";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}")
    public ResponseEntity<Object> update(@PathVariable("id")Integer id,Link link){
        Link l = linkService.findById(id);
        if(l == null){
            return badRequest();
        }
        l.setName(link.getName());
        l.setDomain(link.getDomain());
        l.setSortId(link.getSortId());
        try {
            linkService.update(l);
            return ok();
        }catch (UnaBootException e){
            return badRequest();
        }
    }

    @DeleteMapping("/{id:\\d+}")
    @ResponseBody
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        try {
            linkService.delete(id);
            return ok();
        }catch (UnaBootException e){
            return badRequest();
        }
    }
}
