package com.epam.todo.task.client.dto.cmd;

import lombok.Data;

@Data
public class TaskUpdateStatusCmd {

    private Long taskId;

    private Integer status;

    private Long userId;
}
