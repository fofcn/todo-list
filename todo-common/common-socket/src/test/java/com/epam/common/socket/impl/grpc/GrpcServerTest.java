package com.epam.common.socket.impl.grpc;

import com.epam.common.socket.Endpoint;
import com.epam.common.socket.SocketServer;
import com.epam.common.socket.impl.tcp.grpc.GrpcServer;
import com.epam.common.socket.impl.tcp.grpc.GrpcSocketFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GrpcServerTest {

    private GrpcServer grpcServer;

    @BeforeEach
    public void testConstruct() {
        final Endpoint endpoint = new Endpoint("127.0.0.1", 65533);
        final GrpcSocketFactory grpcSocketFactory = new GrpcSocketFactory();
        SocketServer socketServer = grpcSocketFactory.createSocketServer(endpoint);
        assertNotNull(socketServer);

        grpcServer = (GrpcServer) socketServer;
    }

    @AfterEach
    public void afterEach() {
        grpcServer.shutdown();
        assertFalse(grpcServer.isStarted());
    }

    @Order(1000)
    @Test
    public void testInit() {
        boolean initResult = grpcServer.init(null);
        assertTrue(initResult);
    }

    @Order(999)
    @Test
    public void testStart() {
        grpcServer.init(null);
        grpcServer.start();
        assertTrue(grpcServer.isStarted());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            grpcServer.start();
        });
        assertEquals(exception.getMessage(), "grpc server has started");
    }
}
