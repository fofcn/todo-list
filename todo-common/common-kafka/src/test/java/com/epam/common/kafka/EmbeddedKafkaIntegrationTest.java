package com.epam.common.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest(classes = KafkaConsumerApplication.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class EmbeddedKafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Test
    void givenKafkaDockerContainer_whenSendWithSimpleProducer_thenMessageSent() throws ExecutionException, InterruptedException {
        Message msg = new Message();
        msg.setId(UUID.randomUUID().toString());
        ListenableFuture<SendResult<String, Message>> future = kafkaTemplate.send(UUID.randomUUID().toString(), msg);
        SendResult<String, Message> sendResult = future.get();
        assertNotNull(sendResult.getProducerRecord());
    }

    @Test
    void givenKafkaDockerContainer_whenRevWIIthSimpleConsumer_thenMessageReceived() throws ExecutionException, InterruptedException {
        Message msg = new Message();
        msg.setId(UUID.randomUUID().toString());
        String topic = UUID.randomUUID().toString();
        ListenableFuture<SendResult<String, Message>> future = kafkaTemplate.send(topic, msg);
        future.get();
        ConsumerRecord<String, Message> consumerRecord = kafkaTemplate.receive(topic, 0,0, Duration.ofSeconds(10));
        assertEquals(0, consumerRecord.offset());
    }
}
