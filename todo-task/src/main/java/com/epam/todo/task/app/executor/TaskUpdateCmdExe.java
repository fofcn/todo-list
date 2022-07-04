package com.epam.todo.task.app.executor;

import com.epam.common.core.dto.Response;
import com.epam.common.core.lang.Assert;
import com.epam.todo.task.client.dto.cmd.TaskUpdateCmd;
import com.epam.todo.task.domain.entity.TaskEntity;
import com.epam.todo.task.domain.gateway.TaskGatewayService;
import com.epam.todo.task.domain.valueobject.TaskTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskUpdateCmdExe {

    @Autowired
    private TaskGatewayService taskGatewayService;

    public Response execute(TaskUpdateCmd cmd) {
        TaskEntity taskEntity = taskGatewayService.findTask(cmd.getUserId(), cmd.getTaskId());
        Assert.isNotNull(taskEntity);

        TaskTitle taskTitle = new TaskTitle(cmd.getTitle(), cmd.getSubTitle());
        taskEntity.changeTitle(taskTitle);
        taskGatewayService.updateTaskTitle(taskEntity);
        return Response.buildSuccess();
    }
}
