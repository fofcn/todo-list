package com.epam.todo.task.app.assembler;

import com.epam.todo.task.client.dto.data.TaskListDTO;
import com.epam.todo.task.domain.entity.TaskEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskDTOAssembler {

    public static List<TaskListDTO> assemble(List<TaskEntity> taskEntityList) {
        List<TaskListDTO> taskListDTOList = null;
        if (CollectionUtils.isNotEmpty(taskEntityList)) {
            taskListDTOList = new ArrayList<>(taskEntityList.size());
            List<TaskListDTO> finalTaskListDTOList = taskListDTOList;
            taskEntityList.forEach(entity -> {
                TaskListDTO taskListDTO = new TaskListDTO();
                taskListDTO.setTaskId(entity.getId());
                taskListDTO.setTitle(entity.getTitle().getTitle());
                taskListDTO.setSubTitle(entity.getTitle().getSubTitle());
                taskListDTO.setUserId(entity.getUserId());
                taskListDTO.setStatus(entity.getStatus().getCode());
                finalTaskListDTOList.add(taskListDTO);
            });
        }

        return ListUtils.emptyIfNull(taskListDTOList);
    }
}
