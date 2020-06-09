package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.SortType;
import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.TagService;
import com.ramostear.unaboot.web.UnaBootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/2 0002 14:25.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
@RequestMapping("/admin/tags")
public class TagController extends UnaBootController {

    private final TagService tagService;

    @Autowired
    TagController(TagService tagService){
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String tags(Model model){
        Page<Tag> data = tagService.findAll(pageable("createTime", SortType.DESC));
        model.addAttribute("data",data);
        return "/admin/tag/list";
    }

    @ResponseBody
    @GetMapping(value = "/",params = {"type=json"})
    public List<Tag> tags(){
        List<Tag> tags = tagService.findAll();
        return tags;
    }

    @GetMapping("/create")
    public String create(){
        return "/admin/tag/create";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(Tag tag){
        try {
            tagService.create(tag);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @GetMapping("/{id:\\d+}")
    public String tag(@PathVariable("id")Integer id,Model model){
        Tag tag = tagService.findByIdNullable(id);
        model.addAttribute("tag",tag);
        return "/admin/tag/view";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}")
    public ResponseEntity<Object> tag(@PathVariable("id")Integer id,Tag tag){
        tag.setId(id);
        try {
            tagService.update(tag);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @ResponseBody
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        try {
            tagService.delete(id);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }


}
