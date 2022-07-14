package com.epam.todo.task.domain.valueobject;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum DeleteEnum {
    DELETED(1, "deleted"),
    NORMAL(0, "normal");

    private int code;

    private String desc;

    DeleteEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Optional<DeleteEnum> get(int code) {
        return Arrays.stream(DeleteEnum.values()).filter(status -> status.getCode() == code).findFirst();
    }
}
