package com.epam.todo.gateway.config.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epam.common.core.constant.SecurityConstants;
import com.nimbusds.jose.JWSObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

@Component
@Slf4j
@RequiredArgsConstructor
public class GatewaySecurityFilter implements GlobalFilter, Ordered {

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 非JWT放行不做后续解析处理
        String token = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION_KEY);
        if (!StringUtils.hasLength(token) || !StringUtils.startsWithIgnoreCase(token, SecurityConstants.JWT_PREFIX)) {
            return chain.filter(exchange);
        }

        // 解析JWT获取jti，以jti为key判断redis的黑名单列表是否存在，存在则拦截访问
        token = StringUtils.replace(token, SecurityConstants.JWT_PREFIX, Strings.EMPTY);
        String payload =String.valueOf(JWSObject.parse(token).getPayload());
        JSONObject jsonObject = JSON.parseObject(payload);
        String jti = jsonObject.getString(SecurityConstants.JWT_JTI);
        log.info("jti: {}", jti);
        request = exchange.getRequest().mutate()
                .header(SecurityConstants.JWT_PAYLOAD_KEY, URLEncoder.encode(payload, "UTF-8"))
                .build();
        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

