package com.epam.common.socket.impl.grpc;

import com.epam.common.socket.grpc.HelloRequest;
import org.junit.jupiter.api.Test;

public class GrpcConfigTest {

    private GrpcConfig grpcConfig = new GrpcConfig();

    @Test
    public void testNormalRegisterProtobufSerializer() {
        grpcConfig.registerProtobufSerializer(HelloRequest.class.getName(), HelloRequest.getDefaultInstance());
    }
}
