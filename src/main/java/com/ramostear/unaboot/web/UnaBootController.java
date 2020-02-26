package com.ramostear.unaboot.web;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnaBootController {

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder){
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                        true));
    }

    /**
     * 默认分页
     * @return  Pageable
     */
    protected Pageable page(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        int size = ServletRequestUtils.getIntParameter(request,"size",15);
        int offset = ServletRequestUtils.getIntParameter(request,"offset",1);
        return PageRequest.of(offset-1,size);
    }

    /**
     * 根据排序字段降序排列分页数据
     * @param field     排序字段
     * @return          Pageable
     */
    protected Pageable pageByDesc(String field){
        return initPage(field,0);
    }
    protected Pageable pageByDesc(String field,int size){
        return initPage(field,0,size);
    }

    /**
     * 根据排序字段升序排列分页数据
     * @param field     排序字段
     * @return          Pageable
     */
    protected Pageable pageByAsc(String field){
        return initPage(field,1);
    }
    protected Pageable pageByAsc(String field,int size){
        return initPage(field,1,size);
    }

    /**
     * 路径跳转命令
     * @param path  跳转路径
     * @return      String
     */
    protected String redirect(String path){
        return "redirect:"+path;
    }


    /**
     * 异步请求成功返回值
     * @return
     */
    protected ResponseEntity<Object> ok(){
        return ResponseEntity.ok().build();
    }
    protected ResponseEntity<Object> ok(String msg){
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    /**
     * 异步请求失败返回值
     * @return
     */
    protected ResponseEntity<Object> badRequest(){
        return ResponseEntity.badRequest().build();
    }
    protected ResponseEntity<Object> badRequest(String msg){
        return new ResponseEntity<>(msg,HttpStatus.BAD_REQUEST);
    }

    private Pageable initPage(String field,int type){
        return initPage(field,type,15);
    }

    private Pageable initPage(String field,int type,int _size){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        int size = ServletRequestUtils.getIntParameter(request,"size",_size);
        int offset = ServletRequestUtils.getIntParameter(request,"offset",1);
        if(type == 0){
            return PageRequest.of(offset-1,size,Sort.Direction.DESC,field);
        }else{
            return PageRequest.of(offset-1,size,Sort.Direction.ASC,field);
        }
    }
}
