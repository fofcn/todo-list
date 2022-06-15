package com.epam.todo.task.adapter.web;

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
        cmd.setUserId(JwtUtils.getUserId());
        cmd.setTalentId(JwtUtils.getTalentId());
        return taskService.createTask(cmd);
    }

    @GetMapping("list")
    public MultiResponse<TaskListDTO> listTask() {
        TaskListQuery query = new TaskListQuery();
        query.setDeleted(DeleteEnum.NORMAL.getCode());
        query.setUserId(JwtUtils.getUserId());
        return taskService.listTask(query);
    }

    @DeleteMapping("{taskId}")
    public Response deleteTask(@PathVariable Long taskId) {
        TaskDelCmd cmd = new TaskDelCmd();
        cmd.setTaskId(taskId);
        cmd.setUserId(JwtUtils.getUserId());
        return taskService.delTask(cmd);
    }

    @PutMapping("/{taskId}")
    public Response updateTask(@PathVariable Long taskId, @RequestBody TaskUpdateCmd cmd) {
        cmd.setUserId(JwtUtils.getUserId());
        cmd.setTaskId(taskId);
        return taskService.updateTask(cmd);
    }

    @PutMapping("{taskId}/status/{status}")
    public Response updateTaskStatus(@PathVariable Long taskId, @PathVariable Integer status) {
        TaskUpdateStatusCmd cmd = new TaskUpdateStatusCmd();
        cmd.setStatus(status);
        cmd.setTaskId(taskId);
        cmd.setUserId(JwtUtils.getUserId());
        return taskService.updateTaskStatus(cmd);
    }
}
