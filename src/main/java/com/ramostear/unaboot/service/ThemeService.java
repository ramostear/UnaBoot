package com.ramostear.unaboot.service;

import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.service.base.UnaBootService;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ThemeService extends UnaBootService<Theme,Integer> {
    /**
     * Upload theme archive data
     * @param request
     * @param file
     * @return
     */
    Theme install(HttpServletRequest request, CommonsMultipartFile file);
}
