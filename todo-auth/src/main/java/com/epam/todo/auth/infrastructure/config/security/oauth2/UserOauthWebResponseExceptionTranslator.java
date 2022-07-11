package com.epam.todo.auth.infrastructure.config.security.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

@Component
public class UserOauthWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    private static final Logger log = LoggerFactory.getLogger(UserOauthWebResponseExceptionTranslator.class);

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        log.error("auth exception", e);
        return ResponseEntity.badRequest().build();
    }
}
