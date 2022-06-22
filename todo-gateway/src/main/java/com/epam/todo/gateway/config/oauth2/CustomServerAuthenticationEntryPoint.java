package com.epam.todo.gateway.config.oauth2;

import com.alibaba.fastjson.JSON;
import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.Response;
import com.epam.todo.gateway.config.util.ResponseUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class CustomServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {

        return Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> ResponseUtils.writeErrorInfo(response, ResponseCode.TOKEN_INVALID_OR_EXPIRED));
}
}