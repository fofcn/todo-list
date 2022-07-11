package com.epam.todo.task.infrastructure.model;

import lombok.Getter;

@Getter
public enum StateEnum {

    TODO(1, "TODO"),
    DONE(2, "DONE");

    private int code;

    private String desc;

    StateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
