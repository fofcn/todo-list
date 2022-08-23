package com.epam.todo.common.security.jwt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epam.todo.common.security.constant.JwtConstants;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class JwtHeader extends Claims {

    public void parse(String headerPart) {
        byte[] decode = Base64.decodeBase64(headerPart.getBytes(StandardCharsets.UTF_8));
        String headerJson = new String(decode, StandardCharsets.UTF_8);
        JSONObject headerJsonObj = JSON.parseObject(headerJson);
        setClaimJson(headerJsonObj);
    }

    public String getType() {
        return get(JwtConstants.JWT_TYPE).toString();
    }

    public String getAlgorithm() {
        return get(JwtConstants.ALGORITHM).toString();
    }
}
