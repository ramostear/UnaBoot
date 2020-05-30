package com.ramostear.unaboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@EnableJpaAuditing
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class UnaBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnaBootApplication.class, args);
    }

}
