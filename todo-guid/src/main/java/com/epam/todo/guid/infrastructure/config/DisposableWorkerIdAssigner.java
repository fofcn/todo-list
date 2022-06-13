package com.epam.todo.guid.infrastructure.config;

import com.epam.fans.guid.domain.service.IWorkerNodeService;
import com.epam.fans.guid.infrastructure.model.WorkerNode;
import com.xfvape.uid.utils.DockerUtils;
import com.xfvape.uid.utils.NetUtils;
import com.xfvape.uid.worker.WorkerIdAssigner;
import com.xfvape.uid.worker.WorkerNodeType;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

public class DisposableWorkerIdAssigner implements WorkerIdAssigner {

    private static final Logger logger = LoggerFactory.getLogger(DisposableWorkerIdAssigner.class);

    @Resource
    private IWorkerNodeService workerNodeService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public long assignWorkerId() {
        WorkerNode workerNodeEntity = this.buildWorkerNode();
        this.workerNodeService.save(workerNodeEntity);
        logger.info("Add worker node:" + workerNodeEntity);
        return workerNodeEntity.getId();
    }

    private WorkerNode buildWorkerNode() {
        WorkerNode workerNodeEntity = new WorkerNode();
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setType(WorkerNodeType.CONTAINER.value());
            workerNodeEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeEntity.setPort(DockerUtils.getDockerPort());
        } else {
            workerNodeEntity.setType(WorkerNodeType.ACTUAL.value());
            workerNodeEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeEntity.setPort(System.currentTimeMillis()
                    + "-" + RandomUtils.nextInt(100000));
        }
        workerNodeEntity.setModified(new Date());
        workerNodeEntity.setCreated(new Date());
        return workerNodeEntity;
    }
}

