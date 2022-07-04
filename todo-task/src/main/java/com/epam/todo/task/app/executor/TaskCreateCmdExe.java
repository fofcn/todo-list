package com.epam.todo.task.app.executor;

import com.epam.common.core.dto.Response;
import com.epam.todo.task.client.dto.cmd.TaskCreateCmd;
import com.epam.todo.task.domain.entity.TaskEntity;
import com.epam.todo.task.domain.gateway.TaskGatewayService;
import com.epam.todo.task.domain.service.TaskDomainService;
import com.epam.todo.task.domain.valueobject.DeleteEnum;
import com.epam.todo.task.domain.valueobject.TaskStatus;
import com.epam.todo.task.domain.valueobject.TaskTitle;
import com.epam.todo.task.infrastructure.model.StateEnum;
import com.epam.todo.task.infrastructure.model.Task;
import com.epam.todo.task.infrastructure.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskCreateCmdExe {

    @Autowired
    private TaskGatewayService taskGatewayService;

    @Autowired
    private TaskDomainService taskDomainService;

    public Response execute(TaskCreateCmd cmd) {
        TaskEntity taskEntity = TaskEntity.newEntity(taskDomainService.nextTaskId(),
                cmd.getUserId(),
                new TaskTitle(cmd.getTitle(), cmd.getSubTitle()),
                TaskStatus.TODO,
                DeleteEnum.NORMAL);
        taskGatewayService.saveTask(taskEntity);
        return Response.buildSuccess();
    }
}
