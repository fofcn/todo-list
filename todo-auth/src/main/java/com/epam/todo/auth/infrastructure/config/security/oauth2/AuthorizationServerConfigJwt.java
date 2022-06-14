package com.epam.todo.auth.infrastructure.config.security.oauth2;

import com.alibaba.fastjson.JSON;
import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.Response;
import com.epam.fans.auth.infrastructure.config.security.SecurityProperties;
import com.epam.todo.auth.infrastructure.auth.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.security.KeyPair;
import java.util.*;

@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(SecurityProperties.class)
public class AuthorizationServerConfigJwt extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private UserOauthWebResponseExceptionTranslator userOauthWebResponseExceptionTranslator;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置客户端, 用于client认证
        clients.withClientDetails(clientDetails());
    }

    /**
     * jwt token存储模式
     */
    @Bean
    public JwtTokenStore jwtTokenStore() throws IOException {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * @param endpoints
     * @description token及用户信息存储到redis，当然你也可以存储在当前的服务内存，不推荐
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws IOException {
        // token信息存到redis
        endpoints.tokenStore(jwtTokenStore())
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userDetailsService);

        // 自定义 /oauth/token 异常处理
        endpoints.exceptionTranslator(userOauthWebResponseExceptionTranslator);

        // Token增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        //token存储模式设定 默认为InMemoryTokenStore模式存储到内存中
        endpoints.tokenStore(jwtTokenStore());

        // 获取原有默认授权模式(授权码模式、密码模式、客户端模式、简化模式)的授权者
        List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));

        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);
        endpoints
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain)
                .tokenGranter(compositeTokenGranter)
                .tokenServices(tokenServices(endpoints));
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        //允许表单认证
        oauthServer.allowFormAuthenticationForClients().tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    public DefaultTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) throws IOException {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenEnhancer(tokenEnhancerChain);
    ;    return tokenServices;
    }

    public JwtAccessTokenConverter jwtAccessTokenConverter() throws IOException {
        KeyPair keyPair = keyPair(keyStoreKeyFactory());

        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyStoreKeyFactory keyStoreKeyFactory() throws IOException {
        return new KeyStoreKeyFactory(securityProperties.getJwt().getKeyStore(),
                securityProperties.getJwt().getKeyStorePassword().toCharArray());
    }

    @Bean
    public KeyPair keyPair(KeyStoreKeyFactory keyStoreKeyFactory) {
        return keyStoreKeyFactory.getKeyPair(securityProperties.getJwt().getKeyPairAlias(),
                securityProperties.getJwt().getKeyPairPassword().toCharArray());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpStatus.OK.value());
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            Response resp = Response.buildFailure(ResponseCode.CLIENT_AUTHENTICATION_FAILED.getCode(),
                    ResponseCode.CLIENT_AUTHENTICATION_FAILED.getMsg());
            response.getWriter().print(JSON.toJSONString(resp));
            response.getWriter().flush();
        };
    }

    /**
     * JWT内容增强
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = new HashMap<>();
            Object principal = authentication.getUserAuthentication().getPrincipal();
            if (principal instanceof UserDO) {
                UserDO user = (UserDO) principal;
                additionalInfo.put("userId", user.getUserId());
                additionalInfo.put("username", user.getUsername());
                additionalInfo.put("talentId", user.getTalentId());
            }

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

}
