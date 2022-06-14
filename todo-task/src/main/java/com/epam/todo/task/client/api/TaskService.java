package com.epam.todo.task.client.api;

import com.epam.common.core.dto.MultiResponse;
import com.epam.common.core.dto.Response;
import com.epam.todo.task.client.dto.cmd.TaskCreateCmd;
import com.epam.todo.task.client.dto.cmd.TaskDelCmd;
import com.epam.todo.task.client.dto.cmd.TaskUpdateCmd;
import com.epam.todo.task.client.dto.cmd.TaskUpdateStatusCmd;
import com.epam.todo.task.client.dto.data.TaskListDTO;
import com.epam.todo.task.client.dto.query.TaskListQuery;

public interface TaskService {

    Response createTask(TaskCreateCmd cmd);

    MultiResponse<TaskListDTO> listTask(TaskListQuery query);

    Response delTask(TaskDelCmd cmd);

    Response updateTask(TaskUpdateCmd cmd);

    Response updateTaskStatus(TaskUpdateStatusCmd cmd);
}
