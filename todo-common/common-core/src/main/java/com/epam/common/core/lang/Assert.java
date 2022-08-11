package com.epam.common.core.lang;

import com.epam.common.core.ResponseCode;
import com.epam.common.core.exception.TodoException;

import java.util.Optional;
import java.util.function.Supplier;

public class Assert {

    public static <E extends Throwable> void isNotNull(Object obj, Supplier<? extends E> supplier)
            throws E {
        if (obj == null) {
            throw supplier.get();
        }
    }

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

    public static <T> void isNotNull(T object) {
        if (object == null) {
            throw new TodoException(ResponseCode.PARAMETER_ERROR);
        }
    }

    public static void isTrue(boolean expression) {
        if (!expression) {
            throw new TodoException(ResponseCode.PARAMETER_ERROR);
        }
    }

    public static <T, U> void isEquals(T t, U u) {
        if (!t.equals(u)) {
            throw new TodoException(ResponseCode.PARAMETER_ERROR);
        }
    }

    public static void isNotEmpty(String str) {
        if (str == null || str.length() != 0) {
            throw new TodoException(ResponseCode.PARAMETER_ERROR);
        }
    }

    public static <E extends Throwable> void isNotEmpty(String str, Supplier<? extends E> supplier) throws E {
        if (str == null || str.length() == 0) {
            throw supplier.get();
        }
    }

    public static void checkBetween(String str, int min, int max) {
        if (str.length() < min && str.length() > max) {
            throw new TodoException(ResponseCode.PARAMETER_ERROR);
        }
    }
}
