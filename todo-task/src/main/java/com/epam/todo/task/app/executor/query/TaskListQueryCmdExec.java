package com.epam.todo.task.app.executor.query;

import com.epam.common.core.dto.MultiResponse;
import com.epam.todo.task.client.dto.data.TaskListDTO;
import com.epam.todo.task.client.dto.query.TaskListQuery;
import com.epam.todo.task.infrastructure.model.Task;
import com.epam.todo.task.infrastructure.repository.TaskRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskListQueryCmdExec {

    @Autowired
    private TaskRepository taskRepository;

    public MultiResponse<TaskListDTO> execute(TaskListQuery query) {
        Task task = new Task();
        task.setDeleted(query.getDeleted());
        task.setUserId(query.getUserId());
        Example<Task> example = Example.of(task);
        List<Task> taskList = taskRepository.findAll(example, Sort.by(Sort.Direction.DESC, "lastModifiedTime", "id"));

        // convert model to dto
        List<TaskListDTO> taskDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(taskList)) {
            taskList.forEach(e -> {
                TaskListDTO taskListDTO = new TaskListDTO();
                taskListDTO.setTaskId(e.getId());
                taskListDTO.setTitle(e.getTitle());
                taskListDTO.setSubTitle(e.getSubTitle());
            });
        }

        return MultiResponse.of(taskDtoList);
    }
}
