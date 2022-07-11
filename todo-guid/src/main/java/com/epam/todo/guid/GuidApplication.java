package com.epam.todo.guid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GuidApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuidApplication.class, args);
    }
}
