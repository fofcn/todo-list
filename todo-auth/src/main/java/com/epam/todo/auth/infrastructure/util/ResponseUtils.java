package com.epam.todo.auth.infrastructure.util;

import com.alibaba.fastjson.JSON;
import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils {

    public static void writeErrorInfo(HttpServletResponse response, ResponseCode responseCode) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE, PUT");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization, Origin, X-Requested-With, Content-Type, Accept, Connection, User-Agent, Cookie");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "7200");
        String body = JSON.toJSONString(Response.buildFailure(responseCode));
        response.getWriter().write(body);
    }
}
