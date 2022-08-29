package com.epam.common.kafka;


import org.apache.kafka.clients.producer.Callback;
import org.springframework.kafka.support.SendResult;

public interface Producer<T> {

    SendResult sendSync(Message<T> msg);

    SendResult sendAsync(Message<T> msg, Callback callback);

    SendResult sendOneway(Message<T> msg);
}
