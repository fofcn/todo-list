package com.epam.common.socket.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamedThreadFactory.class);

    private static final LogUncaughtExceptionHandler UNCAUGHT_EX_HANDLER = new LogUncaughtExceptionHandler();

    private final AtomicInteger counter = new AtomicInteger(0);

    private final boolean daemon;

    private final String prefix;

    public NamedThreadFactory(boolean daemon, String prefix) {
        this.daemon = daemon;
        this.prefix = prefix;
    }


    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(this.daemon);
        t.setUncaughtExceptionHandler(UNCAUGHT_EX_HANDLER);
        t.setName(this.prefix + "-" + counter.getAndIncrement());
        return t;
    }

    private static final class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            LOGGER.error("Uncaught exception in thread {}", t, e);
        }
    }
}
