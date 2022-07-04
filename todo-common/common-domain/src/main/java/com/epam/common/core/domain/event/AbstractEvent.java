package com.epam.common.core.domain.event;

import java.util.UUID;

public class AbstractEvent<T> implements Event {

    private T payload;

    public AbstractEvent(T payload) {
        this.payload = payload;
    }

    @Override
    public String msgId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public String version() {
        return Event.super.version();
    }

    public T getPayload() {
        return payload;
    }
}
