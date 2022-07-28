package com.epam.common.socket.impl.grpc;

import com.epam.common.socket.Endpoint;
import com.epam.common.socket.SocketClient;
import com.epam.common.socket.SocketServer;
import com.epam.common.socket.exception.SocketException;
import com.epam.common.socket.grpc.HelloRequest;
import com.epam.common.socket.grpc.HelloResponse;
import com.epam.common.socket.impl.tcp.grpc.GrpcSocketFactory;
import com.epam.common.socket.processor.RequestProcessor;
import com.epam.common.socket.processor.SocketContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GrpcClientTest {

    private SocketClient grpcClient;

    private static GrpcSocketFactory factory = new GrpcSocketFactory();

    private static Endpoint endpoint = new Endpoint("127.0.0.1", 60000);

    @BeforeAll
    public static void beforeAll() {
        factory.registerProtobufSerializer(HelloRequest.class.getName(), HelloRequest.getDefaultInstance());
        factory.registerResponseMarshaller(HelloRequest.class.getName(), HelloResponse.getDefaultInstance());
    }

    @BeforeEach
    public void beforeEach() {
        grpcClient = factory.createRpcClient();
        grpcClient.init(null);
        grpcClient.start();
        assertNotNull(grpcClient);
    }

    @AfterEach
    public void afterEach() {
        grpcClient.shutdown();
    }

    @Test
    public void testSendSyncNormal() {
        // start grpc server
        SocketServer socketServer = factory.createSocketServer(endpoint);
        socketServer.addRequestProcessor(new HelloProcessor());
        socketServer.init(null);
        socketServer.start();

        // send message to grpc server
        HelloRequest request = HelloRequest.newBuilder().setHello("Hello").build();
        try {
            HelloResponse response = (HelloResponse) grpcClient.sendSync(endpoint, request, 60000L);
            assertNotNull(response);
            assertEquals("World", response.getWorld());
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        socketServer.shutdown();
    }

    @Test
    public void testSendSyncException() {
        HelloRequest request = HelloRequest.newBuilder().setHello("Hello").build();
        SocketException socketException = assertThrows(SocketException.class, () -> {
            grpcClient.sendSync(endpoint, request, 60000L);
        });
        assertNotNull(socketException);
    }

    public static class HelloProcessor implements RequestProcessor<HelloRequest> {

        @Override
        public void handleRequest(SocketContext socketContext, HelloRequest request) {
            HelloResponse response = HelloResponse.newBuilder().setWorld("World").build();
            socketContext.sendResponse(response);
        }

        @Override
        public String interest() {
            return HelloRequest.class.getName();
        }
    }
}
