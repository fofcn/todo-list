package com.epam.todo.auth.infrastructure.config.security.oauth2.resourceserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epam.common.core.constant.SecurityConstants;
import com.nimbusds.jose.JWSObject;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Supplier;

public class CustomAuthorizationManager {

    @SneakyThrows
    public AuthorizationDecision check(Supplier<Authentication> authentication, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 预检请求放行
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return new AuthorizationDecision(true);
        }

        // 如果token以"bearer "为前缀，到此方法里说明JWT有效即已认证
        String jwtToken = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION_KEY);
        if (!StringUtils.hasLength(jwtToken) || !StringUtils.startsWithIgnoreCase(jwtToken, SecurityConstants.JWT_PREFIX) ) {
            return new AuthorizationDecision(false);
        }

        // 解析JWT获取jti，以jti为key判断redis的黑名单列表是否存在，存在则拦截访问
        jwtToken = StringUtils.replace(jwtToken, SecurityConstants.JWT_PREFIX, "");
        String payload = String.valueOf(JWSObject.parse(jwtToken).getPayload());
        JSONObject jsonObject = JSON.parseObject(payload);
        Integer expireIn = jsonObject.getInteger("exp");
        // check token if expired
        Instant instant = Instant.ofEpochMilli(expireIn * 1000L);
        LocalDateTime expireTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        if (expireTime.isBefore(LocalDateTime.now())) {
            return new AuthorizationDecision(false);
        }

        return new AuthorizationDecision(true);
    }

}
