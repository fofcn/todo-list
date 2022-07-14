package com.epam.todo.task.domain.entity;

import com.epam.common.core.domain.entity.BaseEntity;
import com.epam.common.core.lang.Assert;
import com.epam.todo.task.domain.valueobject.DeleteEnum;
import com.epam.todo.task.domain.valueobject.TaskStatus;
import com.epam.todo.task.domain.valueobject.TaskTitle;
import com.epam.todo.task.infrastructure.model.StateEnum;
import lombok.Getter;

import java.util.Optional;

@Getter
public class TaskEntity extends BaseEntity {

    private Long userId;

    private TaskTitle title;

    private TaskStatus status;

    private DeleteEnum deleted;

    public TaskEntity(Long id) {
        super(id);
    }

    public static TaskEntity newEntity(Long id, Long userId, TaskTitle taskTitle, TaskStatus status, DeleteEnum deleted) {
        TaskEntity taskEntity = new TaskEntity(id);
        taskEntity.title = taskTitle;
        taskEntity.status = status;
        taskEntity.userId = userId;
        taskEntity.deleted = deleted;
        return taskEntity;
    }

    /**
     * 修改任务状态
     * @param nextStatus
     */
    public void changeStatus(int nextStatus) {
        Optional<TaskStatus> next = TaskStatus.get(nextStatus);
        Assert.isNotNull(next);
        boolean isStatusValid = isValidTransition(status, next.get());
        Assert.isTrue(isStatusValid);
        this.status = next.get();
    }

    public void delete() {
        this.deleted = DeleteEnum.DELETED;
    }

    public boolean isValidTransition(TaskStatus prev, TaskStatus next) {
        if (prev.equals(StateEnum.TODO) && !next.equals(StateEnum.DONE)) {
            return false;
        }

        if (prev.equals(StateEnum.DONE)) {
            return false;
        }

        return true;
    }

    public void changeTitle(TaskTitle title) {
        title.isValid();
        this.title = title;
    }
}
