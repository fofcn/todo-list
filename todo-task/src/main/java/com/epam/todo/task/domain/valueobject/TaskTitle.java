package com.epam.todo.task.domain.valueobject;

import lombok.Data;

@Data
public class TaskTitle {

    private String title;

    private String subTitle;

    public TaskTitle() {

    }

    public TaskTitle(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }
}
