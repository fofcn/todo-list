package com.epam.todo.task.app.executor;

import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.Response;
import com.epam.todo.task.client.dto.cmd.TaskCreateCmd;
import com.epam.todo.task.client.dto.cmd.TaskUpdateStatusCmd;
import com.epam.todo.task.domain.ability.TaskStateAbility;
import com.epam.todo.task.infrastructure.model.Task;
import com.epam.todo.task.infrastructure.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class TaskUpdateStatusCmdExe {

    @Autowired
    private TaskRepository taskRepository;

    public Response execute(TaskUpdateStatusCmd cmd) {
        // 根据任务ID查询任务是否存在且为自己创建的task
        Task task = taskRepository.getById(cmd.getTaskId());
        if (task == null) {
            return Response.buildFailure(ResponseCode.TASK_NOT_EXISTING);
        }

        if (!task.getUserId().equals(cmd.getUserId())) {
            return Response.buildFailure(ResponseCode.TASK_OWNER_ERROR);
        }

        if (!TaskStateAbility.isValidTransition(task.getStatus(), cmd.getStatus())) {
            return Response.buildFailure(ResponseCode.TASK_STATE_TRANSITION_ERROR);
        }

        Task update = new Task();
        task.setId(cmd.getTaskId());
        task.setStatus(cmd.getStatus());
        taskRepository.save(update);
        return Response.buildSuccess();
    }
}
