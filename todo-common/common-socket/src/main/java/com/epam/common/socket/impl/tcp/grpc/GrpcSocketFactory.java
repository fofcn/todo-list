package com.epam.common.socket.impl.tcp.grpc;

import com.epam.common.socket.Endpoint;
import com.epam.common.socket.SocketClient;
import com.epam.common.socket.SocketServer;
import com.google.protobuf.Message;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.util.MutableHandlerRegistry;
import io.netty.util.internal.SystemPropertyUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GrpcSocketFactory {
    static final int RPC_MAX_INBOUND_MESSAGE_SIZE = SystemPropertyUtil.getInt(
            "socket.grpc.max_inbound_message_size.bytes",
            4 * 1024 * 1024);

    private final Map<String, Message> parserClasses = new ConcurrentHashMap<>();

    private final Map<String, Message> responseMarshallers = new ConcurrentHashMap<>();

    public SocketServer createSocketServer(final Endpoint endpoint) {
        final int port = endpoint.getPort();
        final MutableHandlerRegistry handlerRegistry = new MutableHandlerRegistry();
        final Server server = ServerBuilder.forPort(port) //
                .fallbackHandlerRegistry(handlerRegistry) //
                .directExecutor() //
                .maxInboundMessageSize(RPC_MAX_INBOUND_MESSAGE_SIZE) //
                .build();
        return new GrpcServer(server, handlerRegistry, this.parserClasses, this.responseMarshallers);
    }

    public SocketClient createRpcClient() {
        return new GrpcClient(this.parserClasses, responseMarshallers);
    }

    public void registerProtobufSerializer(final String className, final Object... args) {
        this.parserClasses.put(className, (Message) args[0]);
    }

    public void registerResponseMarshaller(final String className, final Object arg) {
        this.responseMarshallers.put(className, (Message) arg);
    }
}
