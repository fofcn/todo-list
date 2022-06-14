package com.epam.todo.auth.adapter.web;

import com.epam.common.core.dto.Response;
import com.epam.common.core.dto.SingleResponse;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@RestController
@AllArgsConstructor
public class PublicKeyController {

    @Autowired
    private KeyPair keyPair;


    @GetMapping("ads")
    public Response testSuccessNullResponse() {
        return Response.buildSuccess();
    }

    @GetMapping("1")
    public Response testFailureResponse() {
        return Response.buildFailure("401", "Authentication failed.");
    }

    @GetMapping("2")
    public SingleResponse<String> testSuccessResponse() {
        return SingleResponse.of("test");
    }

    @GetMapping("/publicKey")
    public Map<String, Object> getPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
