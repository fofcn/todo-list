package com.epam.todo.task.client.dto.cmd;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TaskCreateCmd {

    @NotNull(message = "title can not be null")
    @NotEmpty(message = "title can not be empty")
    @Length(min = 1, max = 60, message = "The length of title is too short or too long")
    private String title;

    @NotNull(message = "subTitle can not be null")
    @NotEmpty(message = "subTitle can not be empty")
    @Length(min = 1, max = 200, message = "The length of subTitle is too short or too long")
    private String subTitle;

    private Long userId;

    private Long talentId;

}
