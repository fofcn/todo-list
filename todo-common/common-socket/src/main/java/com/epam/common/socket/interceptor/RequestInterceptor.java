package com.epam.common.socket.interceptor;

public interface RequestInterceptor<T> {

    /**
     * Intercept request.
     * @param request request.
     */
    void intercept(final T request);
}
