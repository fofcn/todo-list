package com.epam.common.core.lang;

import com.epam.common.core.ResponseCode;
import com.epam.common.core.exception.TodoException;

import java.util.Optional;
import java.util.function.Supplier;

public class Assert {

    public static <E extends Throwable> void isNotNull(Optional<?> optional, Supplier<? extends E> supplier)
            throws E {
        if (optional == null || !optional.isPresent()) {
            throw supplier.get();
        }
    }

    public static void isNotNull(Optional<?> optional) {
        if (optional == null || !optional.isPresent()) {
            throw new TodoException(ResponseCode.PARAMETER_ERROR);
        }
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new TodoException(ResponseCode.PARAMETER_ERROR);
        }
    }
}
