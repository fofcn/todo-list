package com.epam.todo.task.infrastructure.model.converter;

import com.epam.common.core.lang.Assert;
import com.epam.todo.task.domain.entity.TaskEntity;
import com.epam.todo.task.domain.valueobject.DeleteEnum;
import com.epam.todo.task.domain.valueobject.TaskStatus;
import com.epam.todo.task.domain.valueobject.TaskTitle;
import com.epam.todo.task.infrastructure.model.Task;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TaskEntityConverter {

    public TaskEntity deserialize(Task task) {
        TaskTitle taskTitle = new TaskTitle(task.getTitle(), task.getSubTitle());
        Optional<TaskStatus> taskStatus = TaskStatus.get(task.getStatus());
        Assert.isNotNull(taskStatus);
        Optional<DeleteEnum> deleted = DeleteEnum.get(task.getDeleted());
        Assert.isNotNull(deleted);
        return TaskEntity.newEntity(task.getId(), task.getUserId(), taskTitle, taskStatus.get(), deleted.get());
    }

    public Task serialize(TaskEntity taskEntity) {
        Task task = new Task();
        task.setStatus(taskEntity.getStatus().getCode());
        task.setDeleted(taskEntity.getDeleted().getCode());
        task.setUserId(taskEntity.getUserId());
        task.setTitle(taskEntity.getTitle().getTitle());
        task.setSubTitle(taskEntity.getTitle().getSubTitle());
        return task;
    }
}
