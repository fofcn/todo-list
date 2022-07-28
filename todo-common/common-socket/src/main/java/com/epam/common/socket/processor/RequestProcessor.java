package com.epam.common.socket.processor;

import java.util.concurrent.Executor;

public interface RequestProcessor<T> {

    void handleRequest(final SocketContext socketContext, final T request);

    default Executor executor()  {return null;}
}
