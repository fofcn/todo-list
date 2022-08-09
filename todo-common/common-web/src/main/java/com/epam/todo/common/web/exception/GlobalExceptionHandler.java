package com.epam.todo.common.web.exception;

import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.Response;
import com.epam.common.core.exception.TodoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ResponseBody
    @ExceptionHandler(TodoException.class)
    public Response handleException(TodoException e) {
        log.error("todo exception, msg: {}", e.getMessage());
        return Response.buildFailure(e.getErrorCode(), e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response handleException(HttpRequestMethodNotSupportedException e) {
        log.error("http method not support, msg: {}", e.getMessage());
        return Response.buildFailure(ResponseCode.HTTP_METHOD_NOT_SUPPORT);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        log.error("Unknown exception, msg: {}", e.getMessage());
        return Response.buildFailure(ResponseCode.UNKNOWN_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(BadSqlGrammarException.class)
    public Response handleException(BadSqlGrammarException e) {
        log.error("Bad sql grammar exception", e);
        return Response.buildFailure(ResponseCode.BAD_SQL_GRAMMAR);
    }

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Response handleException(DataIntegrityViolationException e) {
        log.error("Data integrity violation exception", e);
        return Response.buildFailure(ResponseCode.DATA_INTEGRITY);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleException(MethodArgumentNotValidException e) {
        log.error("Method argument not valid exception", e);
        StringBuilder msg = new StringBuilder();
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrs = bindingResult.getAllErrors();
        allErrs.forEach(objectError -> {
            FieldError fieldError = (FieldError) objectError;
            msg.append(fieldError.getDefaultMessage());
            msg.append(";");
        });
        return Response.buildFailure(ResponseCode.UNKNOWN_ERROR.getCode(), msg.toString());
    }

    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    public Response handleException(NoHandlerFoundException e) {
        log.error("No handler found exception", e);
        return Response.buildFailure(ResponseCode.HTTP_NOT_FOUND);
    }
}
