package com.epam.common.socket.impl.grpc;

import com.epam.common.socket.Endpoint;
import com.epam.common.socket.SendCallback;
import com.epam.common.socket.SocketClient;
import com.epam.common.socket.SocketServer;
import com.epam.common.socket.exception.SocketException;
import com.epam.common.socket.grpc.HelloRequest;
import com.epam.common.socket.grpc.HelloResponse;
import com.epam.common.socket.impl.tcp.grpc.GrpcSocketFactory;
import com.epam.common.socket.processor.RequestProcessor;
import com.epam.common.socket.processor.SocketContext;
import org.junit.jupiter.api.*;

import java.util.concurrent.*;

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
    @DisplayName("Testcase for sending sync normally")
    public void testSendSyncNormal() {
        SocketServer socketServer = createTestServer();

        // send message to grpc server
        HelloRequest request = HelloRequest.newBuilder().setHello("Hello").build();
        try {
            HelloResponse response = (HelloResponse) grpcClient.sendSync(endpoint, request, 60000L);
            assertNotNull(response);
            assertEquals("World", response.getWorld());
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } finally {
            destroyTestServer(socketServer);
        }
    }

    @Test
    @DisplayName("Testcase for sending async normally")
    public void testSendAsyncNormal() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        SocketServer socketServer = createTestServer();
        HelloRequest request = HelloRequest.newBuilder().setHello("Hello").build();
        try {
            final HelloResponse[] helloResponse = {null};
            final Throwable[] throwables = {null};
            grpcClient.sendAsync(endpoint, request, new SendCallback() {
                @Override
                public void complete(Object result, Throwable err) {
                    helloResponse[0] = (HelloResponse) result;
                    throwables[0] = err;
                    countDownLatch.countDown();
                }

                @Override
                public Executor executor() {
                    return SendCallback.super.executor();
                }
            }, 60000L);

            countDownLatch.await();
            assertNotNull(helloResponse[0]);
            assertNull(throwables[0]);
            assertEquals("World", helloResponse[0].getWorld());
        } catch (InterruptedException e) {
            fail();
        } finally {
            destroyTestServer(socketServer);
        }
    }

    @Test
    public void testSendSyncException() {
        HelloRequest request = HelloRequest.newBuilder().setHello("Hello").build();
        SocketException socketException = assertThrows(SocketException.class, () -> {
            grpcClient.sendSync(endpoint, request, 60000L);
        });
        assertNotNull(socketException);
    }

    private SocketServer createTestServer() {
        // start grpc server
        SocketServer socketServer = factory.createSocketServer(endpoint);
        socketServer.addRequestProcessor(new HelloProcessor());
        socketServer.init(null);
        socketServer.start();

        return socketServer;
    }

    private void destroyTestServer(SocketServer socketServer) {
        socketServer.shutdown();
    }


    public static class HelloProcessor implements RequestProcessor<HelloRequest> {
        private final Executor executor = new ThreadPoolExecutor(1, 1, 60L,
                TimeUnit.SECONDS, new SynchronousQueue<>());
        @Override
        public void handleRequest(SocketContext socketContext, HelloRequest request) {
            HelloResponse response = HelloResponse.newBuilder().setWorld("World").build();
            socketContext.sendResponse(response);
        }

        @Override
        public String interest() {
            return HelloRequest.class.getName();
        }

        @Override
        public Executor executor() {
            return executor;
        }
    }
}
