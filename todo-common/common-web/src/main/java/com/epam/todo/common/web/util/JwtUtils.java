package com.epam.todo.common.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epam.common.core.ResponseCode;
import com.epam.common.core.constant.SecurityConstants;
import com.epam.common.core.exception.TodoException;
import com.nimbusds.jose.JWSObject;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;


public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    private JwtUtils() {

    }

    public static JSONObject getJwtPayload() {
        JSONObject jsonObject = null;
        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(SecurityConstants.AUTHORIZATION_KEY);
        if (!StringUtils.hasLength(token) || !StringUtils.startsWithIgnoreCase(token, SecurityConstants.JWT_PREFIX)) {
            throw new TodoException(ResponseCode.AUTHORIZED_ERROR);
        }

        token = StringUtils.replace(token, SecurityConstants.JWT_PREFIX, Strings.EMPTY);
        LOGGER.info("Get token from request header, token is: [{}]", token);
        try {
            String payload = String.valueOf(JWSObject.parse(token).getPayload());
            LOGGER.info("Get jwt payload from request header, payload is: [{}]", payload);
            if (StringUtils.hasLength(payload)) {
                jsonObject = JSON.parseObject(URLDecoder.decode(payload, StandardCharsets.UTF_8.name()));
            }
        } catch (UnsupportedEncodingException | ParseException e) {
            throw new TodoException(ResponseCode.AUTHORIZED_ERROR);
        }

        return jsonObject;
    }

    public static Long getUserId() {
        JSONObject jwtPayLoad = getJwtPayload();
        if (jwtPayLoad == null) {
            throw new TodoException(ResponseCode.AUTHORIZED_ERROR);
        }

        Long userId = jwtPayLoad.getLong("userId");
        if (userId == null) {
            throw new TodoException(ResponseCode.AUTHORIZED_ERROR);
        }

        return userId;
    }

    public static Long getTalentId() {
        JSONObject jwtPayLoad = getJwtPayload();
        if (jwtPayLoad == null) {
            throw new TodoException(ResponseCode.AUTHORIZED_ERROR);
        }

        Long talentId = jwtPayLoad.getLong("talentId");
        if (talentId == null) {
            throw new TodoException(ResponseCode.AUTHORIZED_ERROR);
        }

        return talentId;
    }


}