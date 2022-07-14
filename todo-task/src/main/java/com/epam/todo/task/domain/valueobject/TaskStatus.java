package com.epam.todo.task.domain.valueobject;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum TaskStatus {
    TODO(1, "todo"),
    DONE(2, "done");

    private int code;

    private String desc;

    TaskStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static Optional<TaskStatus> get(int code) {
        return Arrays.stream(TaskStatus.values()).filter(status -> status.getCode() == code).findFirst();
    }
}
