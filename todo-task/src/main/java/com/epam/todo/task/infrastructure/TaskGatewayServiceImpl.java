package com.epam.todo.task.infrastructure;

import com.epam.common.core.lang.Assert;
import com.epam.todo.task.domain.entity.TaskEntity;
import com.epam.todo.task.domain.gateway.TaskGatewayService;
import com.epam.todo.task.infrastructure.model.Task;
import com.epam.todo.task.infrastructure.model.converter.TaskEntityConverter;
import com.epam.todo.task.infrastructure.repository.TaskRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskGatewayServiceImpl implements TaskGatewayService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskEntityConverter taskEntityConverter;

    @Override
    public List<TaskEntity> findUserAllTask(long userId, int deleted) {
        Task task = new Task();
        task.setDeleted(deleted);
        task.setUserId(userId);
        Example<Task> example = Example.of(task);
        List<Task> taskList = taskRepository.findAll(example, Sort.by(Sort.Direction.DESC, "lastModifiedTime", "id"));

        // convert task to task entity
        List<TaskEntity> taskEntityList = null;
        if (CollectionUtils.isNotEmpty(taskList)) {
            taskEntityList = new ArrayList<>(taskList.size());
            List<TaskEntity> finalTaskEntityList = taskEntityList;
            taskList.forEach(taskDo -> {
                TaskEntity taskEntity = taskEntityConverter.deserialize(taskDo);
                finalTaskEntityList.add(taskEntity);
            });
        }

        return ListUtils.emptyIfNull(taskEntityList);
    }

    @Override
    public void saveTask(TaskEntity taskEntity) {
        Task task = taskEntityConverter.serialize(taskEntity);
        taskRepository.save(task);
    }

    @Override
    public TaskEntity findTask(Long userId, Long taskId) {
        Task task = taskRepository.getById(taskId);
        Assert.isNotNull(task);
        Assert.isEquals(userId, task.getUserId());
        return taskEntityConverter.deserialize(task);
    }

    @Override
    public void deleteTask(TaskEntity taskEntity) {
        Task task = new Task();
        task.setId(taskEntity.getId());
        task.setDeleted(taskEntity.getDeleted().getCode());
        taskRepository.save(task);
    }

    @Override
    public void updateTaskTitle(TaskEntity taskEntity) {
        Task task = new Task();
        task.setId(task.getId());
        task.setTitle(taskEntity.getTitle().getTitle());
        task.setSubTitle(taskEntity.getTitle().getSubTitle());
        taskRepository.save(task);
    }

    @Override
    public void updateTaskStatus(TaskEntity taskEntity) {
        Task task = new Task();
        task.setId(taskEntity.getId());
        task.setStatus(taskEntity.getStatus().getCode());
        taskRepository.save(task);
    }
}
