package com.epam.todo.task.adapter.web;

import com.alibaba.fastjson.JSONObject;
import com.epam.common.core.dto.Response;
import com.epam.todo.common.web.util.JwtUtils;
import com.epam.todo.task.client.api.TaskService;
import com.epam.todo.task.client.dto.cmd.TaskCreateCmd;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeAll
    public static void beforeAll() {
        // static method mock
        MockedStatic<JwtUtils> utilities = mockStatic(JwtUtils.class);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("", "");
        jsonObject.put("", "");
        utilities.when(JwtUtils::getUserId).thenReturn(1L);
        utilities.when(JwtUtils::getTalentId).thenReturn(1L);
    }

    @Test
    public void contextLoads() {
        assertNotNull(taskController);
    }

    @Test
    public void testNormalCreateTask() {
        TaskCreateCmd taskCreateCmd = new TaskCreateCmd();
        taskCreateCmd.setUserId(1L);
        taskCreateCmd.setTalentId(1L);
        taskCreateCmd.setTitle("");
        taskCreateCmd.setSubTitle("");
        when(taskService.createTask(taskCreateCmd)).thenReturn(Response.buildSuccess());

        Response response = taskController.createTask(taskCreateCmd);
        assertNotNull(response);
        assertTrue(response.isSuccess());
    }

}
