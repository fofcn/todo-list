package com.epam.common.socket.impl.grpc;

import com.google.protobuf.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GrpcConfig {

    private final Map<String, Message> parserClasses = new ConcurrentHashMap<>();

    public void registerProtobufSerializer(final String className, final Object... args) {
        this.parserClasses.put(className, (Message) args[0]);
    }
}
