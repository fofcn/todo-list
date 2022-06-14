package com.epam.todo.task.client.dto.cmd;

import lombok.Data;

@Data
public class TaskDelCmd {

    private Long taskId;

    private Long userId;
}
