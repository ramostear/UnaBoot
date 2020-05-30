package com.ramostear.unaboot.web;

import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.common.SortType;
import com.ramostear.unaboot.domain.entity.User;
import org.apache.shiro.SecurityUtils;
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
import java.util.Objects;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/29 0029 18:56.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
public class UnaBootController {
    /**
     * Default datetime format pattern:'yyyy-MM-dd HH:mm:ss'
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @InitBinder
    public void initializeBinder(ServletRequestDataBinder binder){
        binder.registerCustomEditor(Date.class,new CustomDateEditor(DATE_FORMAT,true));
    }

    /**
     * Get HttpServletRequest Object.
     * @return      request
     */
    protected HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * Default pagination method and page size is 15.
     * @return  pageable
     */
    protected Pageable pageable(){
        return pageable(15);
    }

    /**
     * Method for processing page request jump
     * @param path      target path
     * @return          path
     */
    protected String sendRedirect(String path){
        if(path.startsWith("/")){
            return "redirect:"+path;
        }else{
            return "redirect:/"+path;
        }
    }

    protected  ResponseEntity<Object> ok(){
        return ok("success!");
    }
    protected ResponseEntity<Object> ok(String msg){
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    protected ResponseEntity<Object> bad(){
        return bad("failure!");
    }
    protected ResponseEntity<Object> bad(String msg){
        return fail(msg,HttpStatus.BAD_REQUEST);
    }
    protected ResponseEntity<Object> fail(String msg,HttpStatus status){
        return new ResponseEntity<>(msg,status);
    }

    /**
     * Pagination data according to custom page size
     * @param size  page size
     * @return      pageable
     */
    protected Pageable pageable(int size){
        return initializePageable(size);
    }

    /**
     * This method is use the specified attribute to sort,
     * and the default order type is 'DESC',the page size is 15.
     * @param field     sort attribute
     * @return          pageable
     */
    protected Pageable pageable(String field){
        return pageable(field,SortType.DESC);
    }

    /**
     * Paging data according to attribute and sorting type,
     * the default page size is 15.
     * @param field         sort attribute
     * @param type          sort type
     * @return              pageable
     */
    protected Pageable pageable(String field, SortType type){
        return pageable(field,type,15);
    }

    /**
     * Paging the data according to attribute and customize the page size.
     * the default sort type is DESC
     * @param field         sort attribute
     * @param len           customize page size
     * @return              pageable
     */
    protected Pageable pageable(String field,int len){
        return pageable(field,SortType.DESC,len);
    }

    /**
     * Paging data base on attribute,sort type and page size.
     * @param field         sort attribute
     * @param type          sort type
     * @param len           page size
     * @return              pageable
     */
    protected Pageable pageable(String field,SortType type,int len){
        return initializePageable(field, type, len);
    }

    /**
     * Get current login user data.
     * @return          user
     */
    protected User currentUser(){
        return (User) SecurityUtils.getSubject().getSession()
                .getAttribute(Constants.LOGIN_SESSION_KEY);
    }

    private Pageable initializePageable(int len){
        HttpServletRequest request = getRequest();
        int size = ServletRequestUtils.getIntParameter(request,"size",len);
        int offset = ServletRequestUtils.getIntParameter(request,"offset",1);
        return PageRequest.of(offset-1,size);
    }
    private Pageable initializePageable(String field,SortType type,int len){
        HttpServletRequest request = getRequest();
        int size = ServletRequestUtils.getIntParameter(request,"size",len);
        int offset = ServletRequestUtils.getIntParameter(request,"offset",1);
        if(type == SortType.DESC){
            return PageRequest.of(offset-1,size, Sort.Direction.DESC,field);
        }else{
            return PageRequest.of(offset-1,size, Sort.Direction.ASC,field);
        }
    }

}
