package com.ramostear.unaboot.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ramostear.unaboot.common.Constants;
import com.ramostear.unaboot.config.support.CustomFreemarkerConfigurer;
import com.ramostear.unaboot.web.handler.UnaBootInitializeHandler;
import com.ramostear.unaboot.web.handler.UnaBootServletContextHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/28 0028 16:32.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private UnaBootServletContextHandler servletContextHandler;
    @Autowired
    private UnaBootInitializeHandler initializeHandler;


    @Override
    protected void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
        super.configurePathMatch(configurer);
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(servletContextHandler)
                .addPathPatterns("/**");
        registry.addInterceptor(initializeHandler)
                .addPathPatterns("/**")
                .excludePathPatterns("/install.html","/ub-admin/**");
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("file:"+ Constants.UNABOOT_STORAGE_DIR+"storage/")
                .addResourceLocations("file:"+Constants.UNABOOT_STORAGE_DIR+"themes/");
        super.addResourceHandlers(registry);
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        mediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        mediaTypes.add(MediaType.APPLICATION_XML);
        mediaTypes.add(MediaType.IMAGE_GIF);
        mediaTypes.add(MediaType.IMAGE_JPEG);
        mediaTypes.add(MediaType.IMAGE_PNG);
        mediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_XML);
        converter.setSupportedMediaTypes(mediaTypes);

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect
        );

        converter.setFastJsonConfig(fastJsonConfig);
        converters.add(converter);
        super.configureMessageConverters(converters);
    }

    @Bean
    @Primary
    public CustomFreemarkerConfigurer customFreemarkerConfigurer(){
        File file = new File(Constants.UNABOOT_STORAGE_DIR+"themes");
        if(!file.exists()){
            file.mkdirs();
        }
        String[] templateLoaderPaths = {"classpath:/templates/","file:"+Constants.UNABOOT_STORAGE_DIR+"themes/"};
        CustomFreemarkerConfigurer customFreemarkerConfigurer = new CustomFreemarkerConfigurer();
        customFreemarkerConfigurer.setDefaultEncoding(StandardCharsets.UTF_8.name());
        customFreemarkerConfigurer.setTemplateLoaderPaths(templateLoaderPaths);
        return customFreemarkerConfigurer;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize(600000000);
        resolver.setDefaultEncoding("UTF-8");
        return resolver;
    }

}
