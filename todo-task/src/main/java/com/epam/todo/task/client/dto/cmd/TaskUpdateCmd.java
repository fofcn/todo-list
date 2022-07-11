package com.epam.todo.task.client.dto.cmd;

import lombok.Data;

@Data
public class TaskUpdateCmd {

    private Long taskId;

    private String title;

    private String subTitle;

    private Long userId;

}
