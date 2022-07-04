package com.epam.common.core.domain.event;

public interface Event {

    String msgId();

    default String version() {
        return "1_0_0";
    }
}
