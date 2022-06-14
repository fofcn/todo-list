package com.epam.todo.gateway.config.oauth2;

import com.epam.common.core.constant.SecurityConstants;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
@Slf4j
public class CustomReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    /**
     *The permissions corresponding to the resource are saved here and can be obtained from the database
     */
    private static final Map<String, String> AUTH_MAP = Maps.newConcurrentMap();

    @PostConstruct
    public void initAuthMap() {
        AUTH_MAP.put("/user/findAllUsers", "user.userInfo");
        AUTH_MAP.put("/user/addUser", "ROLE_ADMIN");
    }


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (request.getMethod() == HttpMethod.OPTIONS) { // 预检请求放行
            return Mono.just(new AuthorizationDecision(true));
        }

        //Those with wildcards can use this for matching
        PathMatcher pathMatcher = new AntPathMatcher();
        String authorities = AUTH_MAP.get(path);
        log.info("access path: [{}], the required permissions are: [{}]", path, authorities);

        //Option request, release all
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }

        // 如果token以"bearer "为前缀，到此方法里说明JWT有效即已认证
        String jwtToken = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION_KEY);
        if (!StringUtils.hasLength(jwtToken) || !StringUtils.startsWithIgnoreCase(jwtToken, SecurityConstants.JWT_PREFIX) ) {
            return Mono.just(new AuthorizationDecision(false));
        }

        return Mono.just(new AuthorizationDecision(true));

//        return authentication
//                .filter(Authentication::isAuthenticated)
//                .filter(a -> a instanceof JwtAuthenticationToken)
//                .cast(JwtAuthenticationToken.class)
//                .doOnNext(token -> {
//                    System.out.println(token.getToken().getHeaders());
//                    System.out.println(token.getTokenAttributes());
//                })
//                .flatMapIterable(AbstractAuthenticationToken::getAuthorities)
//                .map(GrantedAuthority::getAuthority)
//                .any(authority -> Objects.equals(authority, authorities))
//                .map(AuthorizationDecision::new)
//                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
