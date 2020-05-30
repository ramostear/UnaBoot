package com.ramostear.unaboot.config;

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
 * @author :       ramostear/树下魅狐
 * @version :     Una-Boot-1.3.0
 * <p>This java file was created by ramostear in 2020/5/29 0029 15:53.
 * The following is the description information about this file:</p>
 * <p>description:</p>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ramostear.unaboot"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(
                    new ApiInfoBuilder()
                    .title("")
                    .description("")
                    .version("v1.3.0")
                    .contact(
                        new Contact(
                            "树下魅狐[ramostear]",
                            "https://www.ramostear.com",
                            "ramostear@163.com"
                        )
                    )
                    .license("The Apache2.0 License")
                    .build()
                );
    }
}
