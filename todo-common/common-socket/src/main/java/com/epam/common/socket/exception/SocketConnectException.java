package com.epam.common.socket.exception;

public class SocketConnectException extends SocketException {

    public SocketConnectException() {
    }

    public SocketConnectException(String message) {
        super(message);
    }

    public SocketConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public SocketConnectException(Throwable cause) {
        super(cause);
    }

    public SocketConnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
