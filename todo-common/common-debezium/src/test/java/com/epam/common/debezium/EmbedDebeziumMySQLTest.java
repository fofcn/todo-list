package com.epam.common.debezium;


import com.epam.common.debezium.entity.UserEntity;
import com.epam.common.debezium.repository.UserRepository;
import io.debezium.config.Configuration;
import io.debezium.data.Envelope;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.eclipse.jetty.util.BlockingArrayQueue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.debezium.data.Envelope.FieldName.OPERATION;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EmbedDebeziumTestApplication.class)
class EmbedDebeziumMySQLTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private Configuration customerConnectorConfiguration;

    private BlockingArrayQueue<Envelope.Operation> operationQueue = new BlockingArrayQueue<>(1);

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private DefaultTransactionDefinition transactionDefinition;

    private final TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

    @BeforeEach
    void beforeEach() {
        log.info("before each test case execute, delete all data from database.");
        userRepository.deleteAll();

        log.info("create transaction definition object for manual managing transaction.");
        transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        transactionDefinition.setTimeout(60);

        log.info("initialize countdown latch to 1");
        countDownLatch = new CountDownLatch(1);
    }

    @AfterEach
    void afterEach() {
        log.info("after each test case execute, truncate operationQueue.");
        operationQueue.clear();
    }

    @Test
    void whenCreateUser_thenWaitForBinlog_returnCDCLog() throws InterruptedException {
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            UserEntity userEntity = createRandomUser();
            userRepository.save(userEntity);
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            log.error("Insert user error", e);
            transactionManager.rollback(transactionStatus);
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine =
                DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(customerConnectorConfiguration.asProperties())
                .notifying(this::handleChangeEvent)
                .build();
        executor.execute(debeziumEngine);
        countDownLatch.await(10L, TimeUnit.SECONDS);
        assertEquals(operationQueue.get(0), Envelope.Operation.CREATE);
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
        log.info("Key = '{}' value = '{}'", sourceRecord.key(), sourceRecord.value());
        Struct sourceRecordChangeValue= (Struct) sourceRecord.value();
        if (sourceRecordChangeValue != null) {
            Envelope.Operation operation = Envelope.Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));
            if (operation.equals(Envelope.Operation.CREATE)) {
                operationQueue.add(operation);
                countDownLatch.countDown();
            }
        }
    }

    private UserEntity createRandomUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(UUID.randomUUID().toString().replace("-", ""));
        userEntity.setPassword(UUID.randomUUID().toString());
        return userEntity;
    }

    private UserEntity createFixedUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("test_fixed_user");
        userEntity.setPassword("test_fixed_pwd");
        return userEntity;
    }
}
