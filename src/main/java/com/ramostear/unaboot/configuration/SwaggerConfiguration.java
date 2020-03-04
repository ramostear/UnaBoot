package com.ramostear.unaboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName SwaggerConfiguration
 * @Description 系统文档配置
 * @Author 树下魅狐
 * @Date 2020/3/4 0004 12:07
 * @Version since UnaBoot-1.0
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ramostear.unaboot"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(
                        new ApiInfoBuilder()
                        .title("尤娜博客系统APIs")
                        .description("UnaBoot Blog System APIs")
                        .version("1.2.0")
                        .contact(
                                new Contact("树下魅狐",
                                        "https://www.ramostear.com",
                                        "ramostear@163.com")
                        )
                        .license("The Apache License")
                        .build()
                );
    }
}
