package com.epam.todo.task.adapter.web;

import com.alibaba.fastjson.JSONObject;
import com.epam.common.core.constant.SecurityConstants;
import com.epam.common.core.dto.MultiResponse;
import com.epam.common.core.dto.Response;
import com.epam.todo.common.web.util.JwtUtils;
import com.epam.todo.task.client.api.TaskService;
import com.epam.todo.task.client.dto.cmd.TaskCreateCmd;
import com.epam.todo.task.client.dto.cmd.TaskDelCmd;
import com.epam.todo.task.client.dto.cmd.TaskUpdateCmd;
import com.epam.todo.task.client.dto.cmd.TaskUpdateStatusCmd;
import com.epam.todo.task.client.dto.data.TaskListDTO;
import com.epam.todo.task.client.dto.query.TaskListQuery;
import com.epam.todo.task.infrastructure.model.DeleteEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Response createTask(@RequestBody TaskCreateCmd cmd) {
        return taskService.createTask(cmd);
    }

    @GetMapping
    public MultiResponse<TaskListDTO> listTask() {
        JSONObject payload = JwtUtils.getJwtPayload();
        String jti = payload.getString(SecurityConstants.JWT_JTI); // JWT唯一标识
        TaskListQuery query = new TaskListQuery();
        query.setDeleted(DeleteEnum.NORMAL.getCode());
        query.setUserId();
        return taskService.listTask(query);
    }

    @DeleteMapping("{taskId}")
    public Response deleteTask(@PathVariable Long taskId) {
        TaskDelCmd cmd = new TaskDelCmd();
        cmd.setTaskId(taskId);
        cmd.setUserId();

        return taskService.delTask(cmd);
    }

    @PutMapping
    public Response updateTask(@RequestBody TaskUpdateCmd cmd) {
         cmd.setUserId();
         return taskService.updateTask(cmd);
    }

    @PutMapping("{taskId}/status/{status}")
    public Response updateTaskStatus(@PathVariable Long taskId, @PathVariable Integer status) {
        TaskUpdateStatusCmd cmd = new TaskUpdateStatusCmd();
        cmd.setStatus(status);
        cmd.setTaskId(taskId);
        cmd.setUserId();
        return taskService.updateTaskStatus(cmd);
    }
}
