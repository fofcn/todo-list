package com.epam.common.socket.impl.tcp.grpc;

import com.epam.common.socket.Connection;
import com.epam.common.socket.ConnectionClosedEventListener;
import com.epam.common.socket.SocketServer;
import com.epam.common.socket.interceptor.RequestInterceptor;
import com.epam.common.socket.processor.RequestProcessor;
import com.epam.common.socket.processor.SocketContext;
import com.epam.common.socket.util.NamedThreadFactory;
import com.google.protobuf.Message;
import io.grpc.*;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.ServerCalls;
import io.grpc.util.MutableHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class GrpcServer implements SocketServer {

    private final Logger LOGGER = LoggerFactory.getLogger(GrpcServer.class);

    private static final String EXECUTOR_NAME = "grpc-default-executor";

    private final Server server;

    private final MutableHandlerRegistry handlerRegistry;

    private final Map<String, Message> parserClasses;

    private final Map<String, Message> responseMarshallers;

    private final List<ServerInterceptor> serverInterceptors   = new CopyOnWriteArrayList<>();

    private final List<ConnectionClosedEventListener> closedEventListeners = new CopyOnWriteArrayList<>();

    private final AtomicBoolean started = new AtomicBoolean(false);

    private ExecutorService defaultExecutor;

    public GrpcServer(final Server server, final MutableHandlerRegistry handlerRegistry,
                      final Map<String, Message> parserClasses,
                      final Map<String, Message> responseMarshallers) {
        this.server = server;
        this.handlerRegistry = handlerRegistry;
        this.parserClasses = parserClasses;
        this.responseMarshallers = responseMarshallers;
    }

    @Override
    public boolean init(Void config) {
        this.defaultExecutor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new NamedThreadFactory(true, EXECUTOR_NAME),
                new ThreadPoolExecutor.AbortPolicy());
        return true;
    }

    @Override
    public void start() {
        if (!this.started.compareAndSet(false, true)) {
            throw new IllegalArgumentException("grpc server has started");
        }

        try {
            this.server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isStarted() {
        return this.started.get();
    }

    @Override
    public void shutdown() {
        if (!this.started.compareAndSet(true, false)) {
            return;
        }

        try {
            this.defaultExecutor.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            this.defaultExecutor.shutdownNow();
        }

        try {
            this.server.shutdown();
            this.server.awaitTermination(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            this.server.shutdownNow();
        }
    }

    @Override
    public void addRequestProcessor(RequestProcessor processor) {
        final String interest = processor.interest();
        final Message reqIns = this.parserClasses.get(interest);
        MethodDescriptor<Message, Message> method = MethodDescriptor.<Message, Message>newBuilder()
                .setType(MethodDescriptor.MethodType.UNARY)
                .setFullMethodName(MethodDescriptor.generateFullMethodName(interest, "_call"))
                .setRequestMarshaller(ProtoUtils.marshaller(reqIns))
                .setResponseMarshaller(ProtoUtils.marshaller(responseMarshallers.get(interest)))
                .build();

        final ServerCallHandler<Message, Message> handler = ServerCalls.asyncUnaryCall(
                (request, responseObserver) -> {
                    final SocketAddress remoteAddress = RemoteAddressInterceptor.getRemoteAddress();
                    final Connection conn = ConnectionInterceptor.getCurrentConnection(this.closedEventListeners);

                    final SocketContext rpcCtx = new SocketContext() {

                        @Override
                        public void sendResponse(final Object responseObj) {
                            try {
                                responseObserver.onNext((Message) responseObj);
                                responseObserver.onCompleted();
                            } catch (final Throwable t) {
                                LOGGER.warn("[GRPC] failed to send response.", t);
                            }
                        }

                        @Override
                        public Connection getConnection() {
                            if (conn == null) {
                                throw new IllegalStateException("fail to get connection");
                            }
                            return conn;
                        }

                        @Override
                        public String getRemoteAddress() {
                            // Rely on GRPC's capabilities, not magic (netty channel)
                            return remoteAddress != null ? remoteAddress.toString() : null;
                        }
                    };


                    Executor executor = processor.executor();
                    if (executor == null) {
                        executor = this.defaultExecutor;
                    }

                    if (executor != null) {
                        executor.execute(() -> processor.handleRequest(rpcCtx, request));
                    } else {
                        processor.handleRequest(rpcCtx, request);
                    }
                });

        final ServerServiceDefinition serviceDef = ServerServiceDefinition //
                .builder(interest) //
                .addMethod(method, handler) //
                .build();

        this.handlerRegistry
                .addService(ServerInterceptors.intercept(serviceDef, this.serverInterceptors.toArray(new ServerInterceptor[0])));
    }

    @Override
    public void addRequestInterceptor(RequestInterceptor interceptor) {

    }

    @Override
    public void addConnectionClosedEventListener(final ConnectionClosedEventListener listener) {
        this.closedEventListeners.add(listener);
    }
}
