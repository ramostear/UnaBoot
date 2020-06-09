package com.ramostear.unaboot.web.admin;

import com.ramostear.unaboot.common.SortType;
import com.ramostear.unaboot.common.TaskMethods;
import com.ramostear.unaboot.domain.entity.Schedule;
import com.ramostear.unaboot.exception.UnaBootException;
import com.ramostear.unaboot.service.ScheduleService;
import com.ramostear.unaboot.web.UnaBootController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/3 0003 12:29.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Controller
@RequestMapping("/admin/schedules")
public class ScheduleController extends UnaBootController {

    private final Boolean STOP = false;
    private final Boolean START = true;

    private final ScheduleService scheduleService;

    @Autowired
    ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    @GetMapping("/")
    public String schedules(Model model){
        Page<Schedule> data = scheduleService.findAll(pageable("createTime", SortType.DESC));
        model.addAttribute("data",data);
        return "/admin/schedule/list";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("methods", TaskMethods.values());
        return "/admin/schedule/create";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> create(Schedule schedule){
        if(StringUtils.isBlank(schedule.getCreator())){
            schedule.setCreator(currentUser().getUsername());
        }
        try {
            scheduleService.create(schedule);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @GetMapping("/{id:\\d+}")
    public String schedule(@PathVariable("id")Integer id,Model model){
        Schedule schedule = scheduleService.findByIdNullable(id);
        model.addAttribute("schedule",schedule)
             .addAttribute("methods",TaskMethods.values());
        return "/admin/schedule/view";
    }

    @ResponseBody
    @PostMapping("/{id:\\d+}")
    public ResponseEntity<Object> schedule(@PathVariable("id")Integer id,Schedule schedule){
        schedule.setId(id);
        try {
            scheduleService.update(schedule);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @ResponseBody
    @PutMapping("/{id:\\d+}/stop")
    public ResponseEntity<Object> stop(@PathVariable("id")Integer id){
        try {
            Schedule schedule = scheduleService.findByIdNonNull(id);
            schedule.setState(STOP);
            scheduleService.update(schedule);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @ResponseBody
    @PutMapping("/{id:\\d+}/start")
    public ResponseEntity<Object> start(@PathVariable("id")Integer id){
        try {
            Schedule schedule = scheduleService.findByIdNonNull(id);
            schedule.setState(START);
            scheduleService.update(schedule);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }

    @ResponseBody
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> delete(@PathVariable("id")Integer id){
        try {
            scheduleService.delete(id);
            return ok();
        }catch (UnaBootException e){
            return bad();
        }
    }
}
