package com.epam.todo.task.domain.gateway;

import com.epam.todo.task.domain.entity.TaskEntity;

import java.util.List;

public interface TaskGatewayService {

    /**
     * 查询优化所有的任务
     * @param userId 用户列表
     * @param deleted 删除标志 0：未删除，1：已删除
     * @return 任务实体列表
     */
    List<TaskEntity> findUserAllTask(long userId, int deleted);

    /**
     * 保存一个任务
     * @param taskEntity 任务实体
     */
    void saveTask(TaskEntity taskEntity);
}
