package com.ramostear.unaboot;

import com.ramostear.unaboot.repository.support.UnaBootRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.ramostear.unaboot"})
@EnableJpaRepositories(basePackages = {"com.ramostear.unaboot.repository"},
repositoryBaseClass = UnaBootRepositoryImpl.class)
public class UnaBootProApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(UnaBootProApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(UnaBootProApplication.class);
    }
}
