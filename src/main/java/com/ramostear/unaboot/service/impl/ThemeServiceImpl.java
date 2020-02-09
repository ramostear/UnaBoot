package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.util.ThemeUtils;
import com.ramostear.unaboot.domain.entity.Theme;
import com.ramostear.unaboot.repository.ThemeRepository;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.service.base.UnaBootServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@Transactional
public class ThemeServiceImpl extends UnaBootServiceImpl<Theme,Integer> implements ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeServiceImpl(ThemeRepository repository) {
        super(repository);
        this.themeRepository = repository;
    }

    @Override
    @Transactional
    public Theme install(HttpServletRequest request, CommonsMultipartFile file) {
        Theme theme = ThemeUtils.upload(request,file);
        if(theme == null){
            log.warn("Failed to publish theme file");
            return null;
        }else{
            themeRepository.save(theme);
            return theme;
        }
    }
}
