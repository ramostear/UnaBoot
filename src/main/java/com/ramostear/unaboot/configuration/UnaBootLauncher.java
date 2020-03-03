package com.ramostear.unaboot.configuration;

import com.ramostear.unaboot.common.util.DateTimeUtils;
import com.ramostear.unaboot.common.util.UnaBootUtils;
import com.ramostear.unaboot.domain.entity.Setting;
import com.ramostear.unaboot.service.JobService;
import com.ramostear.unaboot.service.SettingService;
import com.ramostear.unaboot.service.ThemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UnaBootLauncher
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/3/3 0003 4:40
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service
public class UnaBootLauncher implements ApplicationRunner, Ordered, ServletContextAware {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private SettingService settingService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private JobService jobService;

    @Override
    public void run(ApplicationArguments args) {
        if(UnaBootUtils.isInstall()){
            Map<String, Setting> settings = settingService.convertTo();
            ExecutorService executor = new ThreadPoolExecutor(1,1,0, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1),new ThreadPoolExecutor.DiscardOldestPolicy());
            executor.execute(()->{
                //初始化应用参数
                Set<String> keys = settings.keySet();
                keys.forEach((String key)->{
                    servletContext.setAttribute(key,settings.get(key).getValue());
                    log.info("UnaBoot Blog System General Config -- key:{},value:{}",key,settings.get(key).getValue());
                });
                //判断是否安装过
                Setting theme = settings.get("theme");
                if(theme == null){
                    theme = new Setting();
                    theme.setKey("theme");
                    theme.setValue("default");
                    theme.setCreateTime(DateTimeUtils.current());
                    theme.setUpdateTime(DateTimeUtils.current());
                    settingService.create(theme);
                    try {
                        themeService.initDefaultTheme();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //重新构建任务
                jobService.reloadAll();

            });
            executor.shutdown();
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
