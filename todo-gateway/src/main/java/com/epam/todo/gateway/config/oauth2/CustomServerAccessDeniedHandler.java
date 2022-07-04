package com.epam.todo.gateway.config.oauth2;

import com.epam.common.core.ResponseCode;
import com.epam.todo.gateway.config.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class CustomServerAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {

        ServerHttpRequest request = exchange.getRequest();

        return exchange.getPrincipal()
                .doOnNext (principal -> log.info("user: [{}] does not have permission to access: [{}]",
                        principal. getName(), request. getURI()))
                .flatMap(principal -> {
                    ServerHttpResponse response = exchange.getResponse();
                    return ResponseUtils.writeErrorInfo(response, ResponseCode.TOKEN_INVALID_OR_EXPIRED);
                });
}
}
