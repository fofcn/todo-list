package com.epam.todo.common.security.jwt;

import com.alibaba.fastjson.JSON;
import com.epam.todo.common.security.constant.JwtConstants;
import com.nimbusds.jose.JWSHeader;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class JwtHeader extends Claims {

    public void parse(String headerPart) {
        byte[] decode = Base64.decodeBase64(headerPart.getBytes(StandardCharsets.UTF_8));
        String headerJson = new String(decode, StandardCharsets.UTF_8);
        setClaimJson(headerJson);
    }

    public String getType() {
        return get(JwtConstants.JWT_TYPE).toString();
    }

    public String getAlgorithm() {
        return get(JwtConstants.ALGORITHM).toString();
    }

    public JWSHeader toJWSHeader() {
        try {
            return JWSHeader.parse(JSON.toJSONString(getClaimJson()));
        } catch (ParseException e) {
            throw new JwtException(e);
        }
    }
}
