package com.epam.common.socket.processor;

import com.epam.common.socket.Connection;

public interface SocketContext {

    void sendResponse(Object responseObj);

    Connection getConnection();

    String getRemoteAddress();
}
