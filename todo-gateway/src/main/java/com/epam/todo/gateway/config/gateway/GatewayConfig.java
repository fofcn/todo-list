package com.epam.todo.gateway.config.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebFilter;

@Slf4j
@Configuration
public class GatewayConfig {

    @Autowired
    private ExtensionErrorAttributes extensionErrorAttributes;

    @Bean
    public CustomGlobalFilter customGlobalFilter(RouteLocatorBuilder builder) {
        return new CustomGlobalFilter();
    }

    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!CorsUtils.isCorsRequest(request)) {
                return chain.filter(exchange);
            }

            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE, PUT");
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization, Origin, X-Requested-With, Content-Type, Accept, Connection, User-Agent, Cookie");
            headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "7200");
            return chain.filter(exchange);
        };
    }

}
