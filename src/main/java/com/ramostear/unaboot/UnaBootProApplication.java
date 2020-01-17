package com.ramostear.unaboot;

import com.ramostear.unaboot.repository.support.UnaBootRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.ramostear.unaboot"})
@EnableJpaRepositories(basePackages = {"com.ramostear.unaboot.repository"},
repositoryBaseClass = UnaBootRepositoryImpl.class)
public class UnaBootProApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnaBootProApplication.class, args);
    }

}
