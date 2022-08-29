package com.epam.common.swagger;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.boot.starter.autoconfigure.SpringfoxConfigurationProperties;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.Arrays;

@Configuration
@EnableConfigurationProperties(SpringfoxConfigurationProperties.SwaggerConfigurationProperties.class)
public class SpringFoxConfig {

    private final String clientId = "clientId";

    private final String clientSecret = "clientSecret";

    private final String tokenName = "Authentication";

    private final String tokenEndpoint = "http://localhost:8080/token";

    private final String tokenRequestEndpoint = "http://localhost:8080/authorize";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private SecurityScheme securityScheme() {
        AuthorizationCodeGrantBuilder builder = new AuthorizationCodeGrantBuilder();
        builder.tokenEndpoint(tokenEndpointBuilder -> {
            tokenEndpointBuilder.url(tokenEndpoint);
            tokenEndpointBuilder.tokenName(tokenName);
        });

        builder.tokenRequestEndpoint(tokenRequestEndpointBuilder -> {
           tokenRequestEndpointBuilder.url(tokenRequestEndpoint);
           tokenRequestEndpointBuilder.clientIdName("");
           tokenRequestEndpointBuilder.clientSecretName("");
        });
        GrantType grantType = builder.build();

        SecurityScheme oauth = new OAuthBuilder().name(tokenName)
                .grantTypes(Arrays.asList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
        return oauth;
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {
                new AuthorizationScope("read", "for read operations"),
                new AuthorizationScope("write", "for write operations"),
                new AuthorizationScope("foo", "Access foo API") };
        return scopes;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(
                        Arrays.asList(new SecurityReference("Authentication", scopes())))
                .operationSelector(context -> {
                    HttpMethod httpMethod = context.httpMethod();
                    String uri = context.getName();
                    // todo
                    return true;
                })
                .build();

    }
}