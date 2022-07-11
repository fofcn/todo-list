package com.epam.common.core.domain.event;

public interface EventPublisher {

    /**
     * publish a event to event bush
     * @param event event
     */
    void publish(Event event);
}
