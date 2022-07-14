package com.epam.todo.auth.infrastructure.config.security.oauth2.resourceserver;

import com.epam.common.core.ResponseCode;
import com.epam.todo.auth.infrastructure.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomServerAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("", authException);
        ResponseUtils.writeErrorInfo(response, ResponseCode.TOKEN_INVALID_OR_EXPIRED);
    }
}
