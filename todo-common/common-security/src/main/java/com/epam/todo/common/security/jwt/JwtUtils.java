package com.epam.todo.common.security.jwt;

import com.epam.common.core.ResponseCode;
import com.epam.common.core.exception.TodoException;
import com.epam.common.core.lang.Assert;
import com.epam.todo.common.security.constant.SecurityConstants;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;


@Configuration
public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    private JwtUtils() {}

    public static JwtPayload getJwtPayload() {
        Jwt jwt = getJwt();
        return jwt.getPayload();
    }

    public static boolean verifyJwt(Key key) {
        Jwt jwt = getJwt();
        return jwt.verifySign(key);
    }

    private static Jwt getJwt() {
        HttpServletRequest request = getServletRequest();
        String token = request.getHeader(SecurityConstants.AUTHORIZATION_KEY);
        Assert.isNotEmpty(token, () -> new TodoException(ResponseCode.AUTHORIZED_ERROR));
        Assert.isTrue(StringUtils.startsWithIgnoreCase(token, SecurityConstants.JWT_PREFIX),
                () -> new TodoException(ResponseCode.AUTHORIZED_ERROR));

        token = StringUtils.replace(token, SecurityConstants.JWT_PREFIX, Strings.EMPTY);
        LOGGER.info("Get token from request header, token is: [{}]", token);
        return Jwt.of(token);
    }

    private static HttpServletRequest getServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.isNotNull(requestAttributes, () -> new JwtException("Expect in a servlet context"));
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes)requestAttributes).getRequest();
        Assert.isNotNull(httpServletRequest, () -> new JwtException("Expect in a servlet context"));
        return httpServletRequest;
    }
}