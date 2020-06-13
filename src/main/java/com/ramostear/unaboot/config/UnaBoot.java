package com.ramostear.unaboot.config;

import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.service.ScheduleService;
import com.ramostear.unaboot.service.SettingService;
import com.ramostear.unaboot.service.ThemeService;
import com.ramostear.unaboot.util.DateTimeUtils;
import com.ramostear.unaboot.util.UnaBootUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/6/13 0013 1:18.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Slf4j
@Service
public class UnaBoot implements ApplicationRunner, Ordered, ServletContextAware {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private SettingService settingService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private ScheduleService scheduleService;


    @Override
    public void run(ApplicationArguments args){
        if(UnaBootUtils.isInstalled()){
            Map<String, Setting> map = settingService.toMap();
            ExecutorService executorService = new ThreadPoolExecutor(1,1,0, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1),new ThreadPoolExecutor.DiscardOldestPolicy());
            executorService.execute(()->{
                Set<String> keys = map.keySet();
                keys.forEach(key->{
                    servletContext.setAttribute(key,map.get(key).getValue());
                    log.info("Refresh application context attribute -- key:{},value:{}",key,map.get(key).getValue());
                });
                Setting theme = map.get("theme");
                if(theme == null){
                    theme = new Setting();
                    theme.setKey("theme");
                    theme.setValue("default");
                    Date now = DateTimeUtils.now();
                    theme.setUpdateTime(now);
                    theme.setCreateTime(now);
                    settingService.create(theme);
                    themeService.initialize();
                    servletContext.setAttribute("theme",theme.getValue());
                }
                scheduleService.reloadAll();
            });
            executorService.shutdown();
        }
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
