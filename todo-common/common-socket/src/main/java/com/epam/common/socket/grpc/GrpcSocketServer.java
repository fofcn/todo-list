package com.epam.common.socket.grpc;

import com.epam.common.socket.SocketServer;
import com.epam.common.socket.interceptor.RequestInterceptor;
import com.epam.common.socket.processor.RequestProcessor;

public class GrpcSocketServer implements SocketServer {

    public GrpcSocketServer() {
    }

    @Override
    public boolean init(Void config) {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void addRequestProcessor(RequestProcessor processor) {

    }

    @Override
    public void addRequestInterceptor(RequestInterceptor interceptor) {

    }
}
