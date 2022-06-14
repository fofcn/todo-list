package com.epam.todo.gateway.config.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GatewayConfig {

    @Bean
    public CustomGlobalFilter customGlobalFilter(RouteLocatorBuilder builder) {
        return new CustomGlobalFilter();
    }
}
