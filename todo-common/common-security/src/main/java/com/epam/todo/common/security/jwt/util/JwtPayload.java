package com.epam.todo.common.security.jwt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epam.todo.common.security.constant.JwtConstants;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class JwtPayload extends Claims {

    public void parse(String payloadPart) {
        byte[] decode = Base64.decodeBase64(payloadPart.getBytes(StandardCharsets.UTF_8));
        String payloadJson = new String(decode, StandardCharsets.UTF_8);
        JSONObject payloadJsonObj = JSON.parseObject(payloadJson);
        setClaimJson(payloadJsonObj);
    }

    public LocalDateTime getExpireDate() {
        Long expireTimestamp = getLong(JwtConstants.EXPIRES_AT);
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(expireTimestamp),
                TimeZone.getDefault().toZoneId());
    }
}
