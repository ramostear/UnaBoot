package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.exception.UnaBootException;
import com.ramostear.unaboot.domain.entity.PostTag;
import com.ramostear.unaboot.domain.entity.Tag;
import com.ramostear.unaboot.service.PostTagService;
import com.ramostear.unaboot.service.TagService;
import com.ramostear.unaboot.web.UnaBootController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/tag")
public class TagController extends UnaBootController {

    @Autowired
    private TagService tagService;
    @Autowired
    private PostTagService postTagService;


    @GetMapping("/index")
    public String index(Model model){
        Page<Tag> data = tagService.findAll(pageByDesc("createTime"));
        model.addAttribute("data",data);
        return "/admin/tag/index";
    }

    @GetMapping("/create")
    public String create(){
        return "/admin/tag/create";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(Tag tag){
        tagService.createTag(tag);
        if(tag.getId() != null && tag.getId() > 0){
            return ok();
        }else{
            return badRequest();
        }
    }

    @GetMapping("/{id:\\d+}/update")
    public String update(@PathVariable("id") Integer id, Model model){
        Tag tag = tagService.findById(id);
        model.addAttribute("tag",tag);
        return "/admin/tag/update";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}/update")
    public ResponseEntity<Object> update(@PathVariable("id")Integer id,Tag tag){
        tag.setId(id);
        Tag t = tagService.updateTag(tag);
        if(t != null){
            return ok();
        }else{
            return badRequest();
        }
    }

    @ResponseBody
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        try {
            List<PostTag> pts = postTagService.findAllByTagId(id);
            if(!CollectionUtils.isEmpty(pts)){
                postTagService.delete(pts);
            }
            tagService.delete(id);
            return ok();
        }catch (UnaBootException e){
            log.error("Delete tag data error:{}",e.getMessage());
            return badRequest("标签删除失败");
        }
    }
}
