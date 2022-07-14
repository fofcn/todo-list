package com.epam.todo.task.app;

import com.epam.common.core.dto.MultiResponse;
import com.epam.common.core.dto.Response;
import com.epam.todo.task.app.executor.TaskCreateCmdExe;
import com.epam.todo.task.app.executor.TaskDelCmdExe;
import com.epam.todo.task.app.executor.TaskUpdateCmdExe;
import com.epam.todo.task.app.executor.TaskUpdateStatusCmdExe;
import com.epam.todo.task.app.executor.query.TaskListQueryCmdExec;
import com.epam.todo.task.client.api.TaskService;
import com.epam.todo.task.client.dto.cmd.TaskCreateCmd;
import com.epam.todo.task.client.dto.cmd.TaskDelCmd;
import com.epam.todo.task.client.dto.cmd.TaskUpdateCmd;
import com.epam.todo.task.client.dto.cmd.TaskUpdateStatusCmd;
import com.epam.todo.task.client.dto.data.TaskListDTO;
import com.epam.todo.task.client.dto.query.TaskListQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskListQueryCmdExec taskListQueryCmdExec;

    @Autowired
    private TaskCreateCmdExe taskCreateCmdExe;

    @Autowired
    private TaskDelCmdExe taskDelCmdExe;

    @Autowired
    private TaskUpdateCmdExe taskUpdateCmdExe;

    @Autowired
    private TaskUpdateStatusCmdExe taskUpdateStatusCmdExe;

    @Override
    public Response createTask(TaskCreateCmd cmd) {
        return taskCreateCmdExe.execute(cmd);
    }

    @Override
    public MultiResponse<TaskListDTO> listTask(TaskListQuery query) {
        return taskListQueryCmdExec.execute(query);
    }

    @Override
    public Response delTask(TaskDelCmd cmd) {
        return taskDelCmdExe.execute(cmd);
    }

    @Override
    public Response updateTask(TaskUpdateCmd cmd) {
        return taskUpdateCmdExe.execute(cmd);
    }

    @Override
    public Response updateTaskStatus(TaskUpdateStatusCmd cmd) {
        return taskUpdateStatusCmdExe.execute(cmd);
    }
}
