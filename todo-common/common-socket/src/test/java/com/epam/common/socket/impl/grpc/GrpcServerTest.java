package com.epam.common.socket.impl.grpc;

import com.epam.common.socket.Endpoint;
import com.epam.common.socket.SocketServer;
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
        final Endpoint endpoint = new Endpoint("127.0.0.1", 65534);
        final GrpcServerFactory grpcServerFactory = new GrpcServerFactory();
        SocketServer socketServer = grpcServerFactory.createSocketServer(endpoint);
        assertNotNull(socketServer);

        grpcServer = (GrpcServer) socketServer;
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

    @Test
    public void testShutdown() {
        grpcServer.init(null);
        grpcServer.start();
        assertTrue(grpcServer.isStarted());

        grpcServer.shutdown();
        assertFalse(grpcServer.isStarted());

        grpcServer.shutdown();
        assertFalse(grpcServer.isStarted());
    }
}
