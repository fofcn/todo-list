package com.epam.common.socket.impl.grpc;

import com.epam.common.socket.Endpoint;
import com.epam.common.socket.SocketServer;
import com.epam.common.socket.grpc.HelloRequest;
import com.epam.common.socket.grpc.HelloResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GrpcServerFactoryTest {
    GrpcServerFactory serverFactory = new GrpcServerFactory();

    @BeforeEach
    public void beforeEach() {
        this.serverFactory.registerProtobufSerializer(HelloRequest.class.getName(), HelloRequest.getDefaultInstance());
        this.serverFactory.registerProtobufSerializer(HelloResponse.class.getName(), HelloResponse.getDefaultInstance());
    }

    @Test
    public void testNormalCreateSocketServer() {
        final Endpoint endpoint = new Endpoint("127.0.0.1", 65535);
        SocketServer grpcServer = serverFactory.createSocketServer(endpoint);
        assertNotNull(grpcServer);

        grpcServer.init(null);
        grpcServer.start();
    }
}
