package com.epam.todo.guid.infrastructure.handler;

import com.epam.cloud.common.core.dto.Response;
import com.epam.cloud.common.core.exception.FansException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ResponseBody
    @ExceptionHandler(FansException.class)
    public Response handleFansException(FansException fansException) {
        logger.warn("fans exception:【{}}】", fansException.getMessage(), fansException);
        return Response.buildFailure(fansException.getErrorCode(), fansException.getMessage());
    }

    /**
     * http请求的方法不正确
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Response httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        logger.error("http method not support");
        return Response.buildFailure("405", "method not suppert");
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        logger.error("fans exception:【" + e.getMessage() + "】", e);
        return Response.buildFailure("500", e.getMessage() != null ? e.getMessage() : "系统异常");
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Response handleArgumentException(MethodArgumentTypeMismatchException e) {
        logger.error("fans exception:【" + e.getMessage() + "】", e);
        return Response.buildFailure("500", "参数类型异常：" + e.getName());
    }

    @ResponseBody
    @ExceptionHandler(BadSqlGrammarException.class)
    public Response handleException(BadSqlGrammarException e) {
        logger.error("fans exception:【" + e.getMessage() + "】", e);
        return Response.buildFailure("500", "sql异常");
    }

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Response handleSqlException(DataIntegrityViolationException e) {
        logger.error("fans exception:【" + e.getMessage() + "】", e);
        return Response.buildFailure("500", "sql异常");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Response handleMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        logger.error("=================== methodArgumentNotValidExceptionHandler Occur ============");
        return Response.buildFailure("PARAMS_ERROR", ex.getMessage());
    }
}

