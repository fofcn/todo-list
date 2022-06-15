package com.epam.todo.task.client.dto.cmd;

import lombok.Data;

@Data
public class TaskCreateCmd {

    private String title;

    private String subTitle;

    private Long userId;

    private Long talentId;

}
