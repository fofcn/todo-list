package com.epam.todo.task.app.executor;

import com.epam.common.core.dto.Response;
import com.epam.common.core.lang.Assert;
import com.epam.todo.task.client.dto.cmd.TaskUpdateStatusCmd;
import com.epam.todo.task.domain.entity.TaskEntity;
import com.epam.todo.task.domain.gateway.TaskGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskUpdateStatusCmdExe {

    @Autowired
    private TaskGatewayService taskGatewayService;

    public Response execute(TaskUpdateStatusCmd cmd) {
        TaskEntity taskEntity = taskGatewayService.findTask(cmd.getUserId(), cmd.getTaskId());
        Assert.isNotNull(taskEntity);
        taskEntity.changeStatus(cmd.getStatus());
        taskGatewayService.updateTaskStatus(taskEntity);
        return Response.buildSuccess();
    }
}
