package com.epam.todo.task.infrastructure.model;

import lombok.Getter;

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
}
