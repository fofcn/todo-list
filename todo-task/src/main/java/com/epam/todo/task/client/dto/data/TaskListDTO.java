package com.epam.todo.task.client.dto.data;

import lombok.Data;

@Data
public class TaskListDTO {

    private Long taskId;

    private String title;

    private String subTitle;

    private Long userId;

}
