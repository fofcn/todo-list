package com.epam.common.socket;

import com.epam.common.socket.exception.SocketException;

/**
 * socket client
 * The client capability is mainly to send data.
 */
public interface SocketClient extends LifeCycle<SocketConfig> {

    /**
     * Check connection for given address.
     *
     * @param endpoint target address
     * @return true if there is a connection and the connection is active and writable.
     */
    boolean checkConnection(final Endpoint endpoint);

    /**
     * Check connection for given address and async to create a new one if there is no connection.
     * @param endpoint       target address
     * @param createIfAbsent create a new one if there is no connection
     * @return true if there is a connection and the connection is active and writable.
     */
    boolean checkConnection(final Endpoint endpoint, final boolean createIfAbsent);

    /**
     * Close all connections of a address.
     *
     * @param endpoint target address
     */
    void closeConnection(final Endpoint endpoint);

    /**
     * synchronous send an request
     * @param endpoint target host/endpoint
     * @param request the request to send
     * @param timeoutMillis timeout with millisecond time unit
     * @return send result
     */
    default Object sendSync(final Endpoint endpoint, final Object request, final long timeoutMillis) throws SocketException {
        return sendSync(endpoint, request, null, timeoutMillis);
    }

    /**
     * synchronous send an request
     * @param endpoint target host/endpoint
     * @param request the request to send
     * @param sendContext  send context
     * @param timeoutMillis timeout with millisecond time unit
     * @return send result
     */
    Object sendSync(final Endpoint endpoint, final Object request, final SendContext sendContext, final long timeoutMillis) throws SocketException;

    /**
     * asynchronous send a request with a callback
     * @param endpoint target host/endpoint
     * @param request the request to send
     * @param callback send callback
     * @param timeoutMillis timeout with millisecond time unit
     */
    default void sendAsync(final Endpoint endpoint, final Object request, final SendCallback callback, final long timeoutMillis) {
        sendAsync(endpoint, request, callback, null, timeoutMillis);
    }

    /**
     * asynchronous send a request with a callback
     * @param endpoint target host/endpoint
     * @param request the request to send
     * @param callback send callback
     * @param sendContext send context
     * @param timeoutMillis timeout with millisecond time unit
     */
    void sendAsync(final Endpoint endpoint, final Object request, final SendCallback callback, final SendContext sendContext,
                   final long timeoutMillis);
}
