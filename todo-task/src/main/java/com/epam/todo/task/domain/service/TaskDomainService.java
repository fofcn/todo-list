package com.epam.todo.task.domain.service;

import com.epam.todo.task.domain.entity.TaskEntity;
import com.epam.todo.task.domain.valueobject.TaskStatus;
import com.epam.todo.task.infrastructure.model.StateEnum;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 领域服务要不要实现一个接口？
 * 一般来说是不用实现接口的，因为接口是抽象，但是业务服务可以不需要抽象，因为领域服务只有一个实现
 */
@Service
public class TaskDomainService {

    /**
     * 生成一个任务ID
     * @return
     */
    public Long nextTaskId() {
        // todo 调用GUID生成ID
        return new Random().nextLong();
    }

    public boolean isValidTransition(int from, int to) {
        if (from == StateEnum.TODO.getCode() && to != StateEnum.DONE.getCode()) {
            return false;
        }

        if (from == StateEnum.DONE.getCode()) {
            return false;
        }

        return true;
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


}
