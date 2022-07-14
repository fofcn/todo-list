package com.epam.todo.task.adapter.app.executor;

import com.epam.common.core.dto.MultiResponse;
import com.epam.common.core.dto.Response;
import com.epam.todo.task.app.TaskServiceImpl;
import com.epam.todo.task.app.executor.TaskCreateCmdExe;
import com.epam.todo.task.app.executor.TaskDelCmdExe;
import com.epam.todo.task.app.executor.TaskUpdateCmdExe;
import com.epam.todo.task.app.executor.TaskUpdateStatusCmdExe;
import com.epam.todo.task.app.executor.query.TaskListQueryCmdExec;
import com.epam.todo.task.client.dto.cmd.TaskCreateCmd;
import com.epam.todo.task.client.dto.data.TaskListDTO;
import com.epam.todo.task.client.dto.query.TaskListQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    @Mock
    private TaskListQueryCmdExec taskListQueryCmdExec;

    @Mock
    private TaskCreateCmdExe taskCreateCmdExe;

    @Mock
    private TaskDelCmdExe taskDelCmdExe;

    @Mock
    private TaskUpdateCmdExe taskUpdateCmdExe;

    @Mock
    private TaskUpdateStatusCmdExe taskUpdateStatusCmdExe;

    @Test
    public void testCreateTask() {
        TaskCreateCmd cmd = new TaskCreateCmd();
        when(taskCreateCmdExe.execute(cmd)).thenReturn(Response.buildSuccess());
        Response response = taskServiceImpl.createTask(cmd);
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testListTask() {
        TaskListQuery query = new TaskListQuery();
        when(taskListQueryCmdExec.execute(query)).thenReturn(MultiResponse.buildSuccess());
        MultiResponse<TaskListDTO> taskList = taskServiceImpl.listTask(query);
        assertNotNull(taskList);
        assertTrue(taskList.isSuccess());
    }

}
