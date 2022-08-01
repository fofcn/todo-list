package com.epam.common.socket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SendContext {

    private final ConcurrentMap<String, Object> ctx = new ConcurrentHashMap<>();

    public Object put(final String key, final Object value) {
        return this.ctx.put(key, value);
    }

    public Object get(final String key) {
        return this.ctx.get(key);
    }
}
