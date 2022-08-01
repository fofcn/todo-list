package com.epam.todo.auth.infrastructure.handler;

import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.SingleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public SingleResponse handleException(AccessDeniedException e) {
        log.error("access denied", e);
        return SingleResponse.buildFailure(ResponseCode.ACCESS_UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public SingleResponse handleException(Throwable e) {
        log.error("access denied", e);
        return SingleResponse.buildFailure(ResponseCode.ACCESS_UNAUTHORIZED);
    }
}
