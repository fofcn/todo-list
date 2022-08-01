package com.epam.todo.guid.service;

import com.epam.todo.guid.model.WorkerNode;
import com.xfvape.uid.exception.UidGenerateException;

import java.util.List;

public interface IUidGenerateService {

    /**
     * 节点ID
     */
    String WORKER_ID = "workerId";

    /**
     * 解析Uid
     *
     * @param uid Uid
     * @return 解析结果
     */
    String parseUid(Long uid);

    /**
     * 获取WorkerNode
     *
     * @param uid Uid
     * @return 解析结果
     */
    WorkerNode getWorkerNode(Long uid);

    /**
     * 获取Uid
     *
     * @return Uid
     * @throws UidGenerateException 异常
     */
    Long getUid() throws UidGenerateException;

    /**
     * 批量获取Uid
     *
     * @param num 数量
     * @return Uid集合
     * @throws UidGenerateException 异常
     */
    List<Long> batchIds(Integer num) throws UidGenerateException;
}
