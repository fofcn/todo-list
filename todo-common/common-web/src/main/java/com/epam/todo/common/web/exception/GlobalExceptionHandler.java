package com.epam.todo.common.web.exception;

import com.epam.common.core.dto.Response;
import com.epam.common.core.exception.TodoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(TodoException.class)
    public Response handleException(TodoException e) {
        return Response.buildFailure("", e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response handleException(HttpRequestMethodNotSupportedException e) {
        log.error("http method not support, msg: {}", e.getMessage());
        return Response.buildFailure("", e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        log.error("Unknown exception, msg: {}", e.getMessage());
        return Response.buildFailure("", e.getMessage() != null ? e.getMessage() : "Unknown error");
    }

    @ResponseBody
    @ExceptionHandler(BadSqlGrammarException.class)
    public Response handleException(BadSqlGrammarException e) {
        log.error("Bad sql grammar exception", e);
        return Response.buildFailure("", "BadSqlGrammarException");
    }

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Response handleException(DataIntegrityViolationException e) {
        log.error("Data integrity violation exception", e);
        return Response.buildFailure("", "DataIntegrityViolationException");
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleException(MethodArgumentNotValidException e) {
        log.error("Method argument not valid exception", e);
        return Response.buildFailure("", "DataIntegrityViolationException");
    }
}
