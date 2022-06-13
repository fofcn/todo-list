package com.epam.common.core.exception;

public class TodoException extends RuntimeException {

    private String errorCode;

    private String message;

    public TodoException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
