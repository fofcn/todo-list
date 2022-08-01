package com.epam.todo.task.adapter.app;

import com.epam.todo.task.app.assembler.TaskDTOAssembler;
import com.epam.todo.task.client.dto.data.TaskListDTO;
import com.epam.todo.task.domain.entity.TaskEntity;
import com.epam.todo.task.domain.valueobject.DeleteEnum;
import com.epam.todo.task.domain.valueobject.TaskStatus;
import com.epam.todo.task.domain.valueobject.TaskTitle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskDTOAssemblerTest {

    @Test
    @DisplayName("Task DTO Assembler normal test")
    public void testAssemble() {
        TaskEntity taskEntity = TaskEntity.newEntity(1L, 1L, new TaskTitle(), TaskStatus.TODO, DeleteEnum.NORMAL);
        List<TaskEntity> taskEntityList = Arrays.asList(taskEntity);
        List<TaskListDTO> taskListDTOList = TaskDTOAssembler.assemble(taskEntityList);
        assertNotNull(taskListDTOList);
        assertEquals(1, taskListDTOList.size());
        assertEquals(taskEntity.getUserId(), taskListDTOList.get(0).getUserId());
    }

    @Test
    @DisplayName("Task DTO Assembler return empty list")
    public void testReturnEmptyList() {
        List<TaskListDTO> emptyReturn = TaskDTOAssembler.assemble(null);
        assertNotNull(emptyReturn);
        assertEquals(0, emptyReturn.size());
    }
}
