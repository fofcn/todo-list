package com.epam.todo.task.app.executor;

import com.epam.common.core.dto.Response;
import com.epam.common.core.lang.Assert;
import com.epam.todo.task.client.dto.cmd.TaskDelCmd;
import com.epam.todo.task.domain.entity.TaskEntity;
import com.epam.todo.task.domain.gateway.TaskGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskDelCmdExe {

    @Autowired
    private TaskGatewayService taskGatewayService;

    public Response execute(TaskDelCmd cmd) {
        TaskEntity taskEntity = taskGatewayService.findTask(cmd.getUserId(), cmd.getTaskId());
        Assert.isNotNull(taskEntity);
        taskEntity.delete();
        taskGatewayService.deleteTask(taskEntity);
        return Response.buildSuccess();
    }
}
