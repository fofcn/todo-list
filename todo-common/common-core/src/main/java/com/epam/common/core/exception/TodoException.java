package com.epam.common.core.exception;

import com.epam.common.core.ResponseCode;

public class TodoException extends RuntimeException {

    private String errorCode;

    private String message;

    public TodoException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public TodoException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.errorCode = responseCode.getCode();
        this.message = responseCode.getMsg();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
