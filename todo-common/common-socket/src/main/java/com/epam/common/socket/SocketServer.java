package com.epam.common.socket;

import com.epam.common.socket.interceptor.RequestInterceptor;
import com.epam.common.socket.processor.RequestProcessor;

/**
 * socket server
 *
 */
public interface SocketServer extends LifeCycle<Void> {

    /**
     * Add a user's request processor
     * @param processor request processor
     */
    void addRequestProcessor(final RequestProcessor<?> processor);

    /**
     * Add a user's request interceptor
     * @param interceptor request interceptor
     */
    void addRequestInterceptor(final RequestInterceptor interceptor);

    void registerConnectionClosedEventListener(final ConnectionClosedEventListener listener);
}
