package com.epam.todo.task.app.executor.query;

import com.epam.common.core.dto.MultiResponse;
import com.epam.todo.task.app.assembler.TaskDTOAssembler;
import com.epam.todo.task.client.dto.data.TaskListDTO;
import com.epam.todo.task.client.dto.query.TaskListQuery;
import com.epam.todo.task.domain.entity.TaskEntity;
import com.epam.todo.task.domain.gateway.TaskGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskListQueryCmdExec {

    @Autowired
    private TaskGatewayService taskGatewayService;

    public MultiResponse<TaskListDTO> execute(TaskListQuery query) {
        // find
        List<TaskEntity> taskList = taskGatewayService.findUserAllTask(query.getUserId(), query.getDeleted());
        // convert entity to dto
        List<TaskListDTO> taskListDTOList = TaskDTOAssembler.assemble(taskList);
        return MultiResponse.of(taskListDTOList);
    }
}
