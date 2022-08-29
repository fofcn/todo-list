package com.epam.common.kafka;

import java.util.Map;

public class Message<T> {

    private String id;

    private Map<String, Object> header;

    private T payload;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
