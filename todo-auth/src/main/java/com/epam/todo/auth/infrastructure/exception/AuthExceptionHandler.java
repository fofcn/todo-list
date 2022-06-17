package com.epam.todo.auth.infrastructure.exception;

import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthExceptionHandler {

    /**
     * 用户不存在
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UsernameNotFoundException.class)
    public Response handleUsernameNotFoundException(UsernameNotFoundException e) {
        return Response.buildFailure(ResponseCode.USER_NOT_EXIST);
    }

    /**
     * 用户名和密码异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(InvalidGrantException.class)
    public Response handleInvalidGrantException(InvalidGrantException e) {
        return Response.buildFailure(ResponseCode.USERNAME_OR_PASSWORD_ERROR);
    }


    /**
     * 账户异常(禁用、锁定、过期)
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public Response handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        return Response.buildFailure(ResponseCode.ACCESS_UNAUTHORIZED);
    }

    /**
     * token 无效或已过期
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({InvalidTokenException.class})
    public Response handleInvalidTokenExceptionException(InvalidTokenException e) {
        return Response.buildFailure(ResponseCode.TOKEN_INVALID_OR_EXPIRED);
    }

}
