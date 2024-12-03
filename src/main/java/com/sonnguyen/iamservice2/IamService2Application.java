package com.sonnguyen.iamservice2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableFeignClients
@SpringBootApplication
@EnableWebMvc
public class IamService2Application {

    public static void main(String[] args) {
        SpringApplication.run(IamService2Application.class, args);
    }

}
