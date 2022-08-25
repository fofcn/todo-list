package com.epam.todo.common.security.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epam.common.core.ResponseCode;
import com.epam.common.core.exception.TodoException;
import com.epam.common.core.lang.Assert;
import com.epam.todo.common.security.constant.SecurityConstants;
import com.nimbusds.jose.JWSObject;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;


public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    private JwtUtils() {}

    public static JSONObject getJwtPayload() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.isNotNull(requestAttributes, () -> new JwtException("Expect in a servlet context"));
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)requestAttributes).getRequest();
        Assert.isNotNull(httpServletRequest, () -> new JwtException("Expect in a servlet context"));

        JSONObject jsonObject = null;
        String token = httpServletRequest.getHeader(SecurityConstants.AUTHORIZATION_KEY);
        Assert.isNotEmpty(token, () -> new TodoException(ResponseCode.AUTHORIZED_ERROR));
        Assert.isTrue(StringUtils.startsWithIgnoreCase(token, SecurityConstants.JWT_PREFIX),
                () -> new TodoException(ResponseCode.AUTHORIZED_ERROR));

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

}