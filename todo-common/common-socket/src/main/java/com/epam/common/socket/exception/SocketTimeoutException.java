package com.epam.common.socket.exception;

public class SocketTimeoutException extends SocketException {

    public SocketTimeoutException() {
    }

    public SocketTimeoutException(String message) {
        super(message);
    }

    public SocketTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketTimeoutException(Throwable cause) {
        super(cause);
    }

    public SocketTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
