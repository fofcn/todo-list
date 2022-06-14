package com.epam.todo.task.app.executor;

import com.epam.common.core.dto.Response;
import com.epam.todo.task.client.dto.cmd.TaskCreateCmd;
import com.epam.todo.task.infrastructure.model.DeleteEnum;
import com.epam.todo.task.infrastructure.model.Task;
import com.epam.todo.task.infrastructure.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskCreateCmdExe {

    @Autowired
    private TaskRepository taskRepository;

    public Response execute(TaskCreateCmd cmd) {
        Task task = new Task();
        task.setUserId(cmd.getUserId());
        task.setTitle(cmd.getTitle());
        task.setSubTitle(cmd.getSubTitle());
        task.setCreateTime(LocalDateTime.now());
        task.setLastModifiedTime(LocalDateTime.now());
        task.setDeleted(DeleteEnum.NORMAL.getCode());
        taskRepository.save(task);
        return Response.buildSuccess();
    }
}
