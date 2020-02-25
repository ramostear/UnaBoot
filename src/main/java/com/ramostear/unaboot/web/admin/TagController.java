package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.service.TagService;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tag")
public class TagController extends UnaBootController {

    @Autowired
    private TagService tagService;

    @GetMapping("/index")
    public String index(Model model){
        Page<Tag> data = tagService.findAll(pageByDesc("createTime"));
        model.addAttribute("data",data);
        return "/admin/tag/index";
    }

}
