package com.epam.common.socket;

import java.util.concurrent.Executor;

public interface SendCallback {

    void complete(final Object result, final Throwable err);

    default Executor executor() {
        return null;
    }
}
