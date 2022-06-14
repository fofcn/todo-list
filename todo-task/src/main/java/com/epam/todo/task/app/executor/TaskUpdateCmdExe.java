package com.epam.todo.task.app.executor;

import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.Response;
import com.epam.todo.task.client.dto.cmd.TaskCreateCmd;
import com.epam.todo.task.client.dto.cmd.TaskUpdateCmd;
import com.epam.todo.task.infrastructure.model.DeleteEnum;
import com.epam.todo.task.infrastructure.model.Task;
import com.epam.todo.task.infrastructure.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskUpdateCmdExe {

    @Autowired
    private TaskRepository taskRepository;

    public Response execute(TaskUpdateCmd cmd) {
        // 根据任务ID查询任务是否存在且为自己创建的task
        Task task = taskRepository.getById(cmd.getTaskId());
        if (task == null) {
            return Response.buildFailure(ResponseCode.TASK_NOT_EXISTING);
        }

        if (!task.getUserId().equals(cmd.getUserId())) {
            return Response.buildFailure(ResponseCode.TASK_OWNER_ERROR);
        }

        // 逻辑删除任务
        Task update = new Task();
        task.setId(cmd.getTaskId());
        task.setTitle(cmd.getTitle());
        task.setSubTitle(cmd.getSubTitle());
        taskRepository.save(update);
        return Response.buildSuccess();
    }
}
