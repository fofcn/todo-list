package com.epam.todo.auth.app.auth.executor;

import com.alibaba.fastjson.JSONObject;
import com.epam.common.core.ResponseCode;
import com.epam.common.core.constant.SecurityConstants;
import com.epam.common.core.dto.SingleResponse;
import com.epam.todo.auth.client.dto.cmd.AuthLoginCmd;
import com.epam.todo.auth.client.dto.data.AuthTokenDTO;
import com.epam.todo.auth.infrastructure.config.AuthConfig;
import com.epam.todo.auth.infrastructure.exception.CustomRestResponseErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthLoginCmdExe {

    @Autowired
    private AuthConfig authConfig;

    @Autowired
    private TokenEndpoint tokenEndpoint;;

    @Autowired
    private RestTemplate restTemplate;

    public SingleResponse<AuthTokenDTO> execute(AuthLoginCmd cmd) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        parameters.add("client_id", authConfig.getClientId());
        parameters.add("client_secret", authConfig.getClientSecret());
        parameters.add("username", cmd.getUsername());
        parameters.add("password", cmd.getPassword());
        parameters.add("grant_type", "password");
        parameters.add(SecurityConstants.OAUTH_REFRESH_TOKEN, SecurityConstants.OAUTH_REFRESH_TOKEN);
        parameters.add("remember-me", "1");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);
        restTemplate.setErrorHandler(new CustomRestResponseErrorHandler());
        ResponseEntity<JSONObject> response = restTemplate.exchange("http://localhost:40002/oauth/token",
                HttpMethod.POST, requestEntity, JSONObject.class);
        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            return SingleResponse.buildFailure(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }

        JSONObject result = response.getBody();
        if (result == null) {
            return SingleResponse.buildFailure(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
        }

        AuthTokenDTO dto = new AuthTokenDTO();
        dto.setAccessToken(result.getString("access_token"));
        dto.setRefreshToken(result.getString(SecurityConstants.OAUTH_REFRESH_TOKEN));
        dto.setType(result.getString("token_type").substring(0, 1).toUpperCase() +
                result.getString("token_type").substring(1));
        dto.setExpireIn(result.getInteger("expires_in"));
        return SingleResponse.of(dto);
    }
}
