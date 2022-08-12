package com.epam.common.encrypto;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ThreadPoolExecutorTest {

    private ThreadPoolExecutor threadPoolExecutor;

    @Test
    void testConstructor() {
        int corePoolSize = 1;
        int maximumPoolSize = 1;
        long keepAliveTime = 60;
        TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
        BlockingQueue blockingQueue = new ArrayBlockingQueue(10);
        ThreadFactory threadFactory = r -> new Thread(r);
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                keepAliveTimeUnit, blockingQueue, threadFactory, rejectedExecutionHandler);
        this.threadPoolExecutor.execute(() -> {
            System.out.println();
        });
    }
}
