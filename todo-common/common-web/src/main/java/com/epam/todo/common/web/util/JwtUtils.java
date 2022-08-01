package com.epam.todo.common.web.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epam.common.core.ResponseCode;
import com.epam.common.core.constant.SecurityConstants;
import com.epam.common.core.exception.TodoException;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class JwtUtils {

    public static JSONObject getJwtPayload() {
        JSONObject jsonObject = null;
        String payload = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader(SecurityConstants.JWT_PAYLOAD_KEY);
        if (StringUtils.hasLength(payload)) {
            try {
                jsonObject = JSON.parseObject(URLDecoder.decode(payload, StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                throw new TodoException(ResponseCode.AUTHORIZED_ERROR);
            }
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