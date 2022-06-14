package com.epam.todo.auth.app.auth.executor;

import com.alibaba.fastjson.JSONObject;
import com.epam.common.core.dto.SingleResponse;
import com.epam.todo.auth.client.dto.cmd.AuthRefreshTokenCmd;
import com.epam.todo.auth.client.dto.data.AuthRefreshTokenDTO;
import com.epam.todo.auth.client.dto.data.AuthTokenDTO;
import com.epam.todo.auth.infrastructure.config.AuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthRefreshTokenCmdExec {

    @Autowired
    private AuthConfig authConfig;

    @Autowired
    private TokenEndpoint tokenEndpoint;;

    @Autowired
    private RestTemplate restTemplate;

    public SingleResponse<AuthRefreshTokenDTO> execute(AuthRefreshTokenCmd cmd) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        parameters.add("client_id", authConfig.getClientId());
        parameters.add("client_secret", authConfig.getClientSecret());
        parameters.add("username", "");
        parameters.add("password", "");
        parameters.add("grant_type", "refresh_token");
;        parameters.add("refresh_token", cmd.getRefreshToken());
        parameters.add("remember-me", "1");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);
//        restTemplate.setErrorHandler(new CustomRestResponseErrorHandler());
        ResponseEntity<JSONObject> response = restTemplate.exchange("http://localhost:40002/oauth/token",
                HttpMethod.POST, requestEntity, JSONObject.class);
        JSONObject result = response.getBody();
        AuthRefreshTokenDTO dto = new AuthRefreshTokenDTO();
        dto.setAccessToken(result.getString("access_token"));
        dto.setRefreshToken(result.getString("refresh_token"));
        dto.setType(result.getString("token_type"));
        dto.setExpireIn(result.getInteger("expires_in"));
        return SingleResponse.of(dto);
    }
}
