package com.epam.todo.task.client.dto.query;

import lombok.Data;

@Data
public class TaskListQuery {

    private Long userId;

    private Integer deleted;

}
