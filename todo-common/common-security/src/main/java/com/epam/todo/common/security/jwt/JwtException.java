package com.epam.todo.common.security.jwt;

public class JwtException extends RuntimeException {
    public JwtException(String s) {
        super(s);
    }

    public JwtException(Throwable cause) {
        super(cause);
    }
}
