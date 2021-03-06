package com.epam.todo.auth.infrastructure.config.security.oauth2;

import com.alibaba.fastjson.JSON;
import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.Response;
import com.epam.fans.auth.infrastructure.config.security.SecurityProperties;
import com.epam.todo.auth.infrastructure.config.security.CustomUserDetails;
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
import org.springframework.security.oauth2.provider.token.*;
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
        // ???????????????, ??????client??????
        clients.withClientDetails(clientDetails());
    }

    /**
     * jwt token????????????
     */
    @Bean
    public JwtTokenStore jwtTokenStore() throws IOException {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * @param endpoints
     * @description token????????????????????????redis???????????????????????????????????????????????????????????????
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws IOException {
        // token????????????redis
        endpoints.tokenStore(jwtTokenStore())
                .authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userDetailsService);

        // ????????? /oauth/token ????????????
        endpoints.exceptionTranslator(userOauthWebResponseExceptionTranslator);
        endpoints.setClientDetailsService(clientDetails());

        // Token??????
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
        tokenEnhancers.add(tokenEnhancer());
        tokenEnhancers.add(jwtAccessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);

        //token?????????????????? ?????????InMemoryTokenStore????????????????????????
        endpoints.tokenStore(jwtTokenStore());

        // ??????????????????????????????(???????????????????????????????????????????????????????????????)????????????
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
        //??????????????????
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
        // set client details service
        // fixed token expire time
        tokenServices.setClientDetailsService(clientDetails());
        tokenServices.setTokenStore(endpoints.getTokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        return tokenServices;
    }

    public JwtAccessTokenConverter jwtAccessTokenConverter() throws IOException {
        KeyPair keyPair = keyPair(keyStoreKeyFactory());

        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair);
        // fixed refresh token problem: https://stackoverflow.com/questions/54279755/getprincipal-method-returning-username-instead-of-userdetails
        ((DefaultAccessTokenConverter)jwtAccessTokenConverter.getAccessTokenConverter()).setUserTokenConverter(userAuthenticationConverter());
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
     * JWT????????????
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = new HashMap<>();
            Object principal = authentication.getUserAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails user = (CustomUserDetails) principal;
                additionalInfo.put("userId", user.getUserId());
                additionalInfo.put("username", user.getUsername());
                additionalInfo.put("talentId", user.getTalentId());
            }

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    @Bean
    public UserAuthenticationConverter userAuthenticationConverter() {
        DefaultUserAuthenticationConverter defaultUserAuthenticationConverter = new DefaultUserAuthenticationConverter();
        defaultUserAuthenticationConverter.setUserDetailsService(userDetailsService);
        return defaultUserAuthenticationConverter;
    }

}
