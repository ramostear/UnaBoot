package com.ramostear.unaboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class UnaBootProApplication  extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(UnaBootProApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(UnaBootProApplication.class);
    }
}
