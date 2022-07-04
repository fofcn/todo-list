package com.epam.todo.task.app.executor;

import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.Response;
import com.epam.todo.task.client.dto.cmd.TaskUpdateStatusCmd;
import com.epam.todo.task.domain.service.TaskDomainService;
import com.epam.todo.task.infrastructure.model.Task;
import com.epam.todo.task.infrastructure.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskUpdateStatusCmdExe {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskDomainService taskDomainService;

    public Response execute(TaskUpdateStatusCmd cmd) {
        // 根据任务ID查询任务是否存在且为自己创建的task
        Task task = taskRepository.getById(cmd.getTaskId());
        if (task == null) {
            return Response.buildFailure(ResponseCode.TASK_NOT_EXISTING);
        }

        if (!task.getUserId().equals(cmd.getUserId())) {
            return Response.buildFailure(ResponseCode.TASK_OWNER_ERROR);
        }

        if (!taskDomainService.isValidTransition(task.getStatus(), cmd.getStatus())) {
            return Response.buildFailure(ResponseCode.TASK_STATE_TRANSITION_ERROR);
        }

        task.setId(cmd.getTaskId());
        task.setStatus(cmd.getStatus());
        task.setLastModifiedTime(LocalDateTime.now());
        taskRepository.save(task);
        return Response.buildSuccess();
    }
}
