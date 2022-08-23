package com.epam.todo.common.security.constant;

public interface SecurityConstants {

    /**
     * 认证请求头key
     */
    String AUTHORIZATION_KEY = "Authorization";

    /**
     * JWT令牌前缀
     */
    String JWT_PREFIX = "Bearer ";


    /**
     * Basic认证前缀
     */
    String BASIC_PREFIX = "Basic ";

    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";

    /**
     * oauth2 refresh token key
     */
    String OAUTH_REFRESH_TOKEN = "refresh_token";
}
