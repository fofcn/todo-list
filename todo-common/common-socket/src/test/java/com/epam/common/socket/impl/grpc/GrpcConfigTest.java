package com.epam.common.socket.impl.grpc;

import com.epam.common.socket.grpc.HelloRequest;
import com.epam.common.socket.impl.tcp.grpc.GrpcConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GrpcConfigTest {

    private GrpcConfig grpcConfig = new GrpcConfig();

    @Test
    void testNormalRegisterProtobufSerializer() {
        grpcConfig.registerProtobufSerializer(HelloRequest.class.getName(), HelloRequest.getDefaultInstance());
        assertNotNull(grpcConfig);
    }
}
