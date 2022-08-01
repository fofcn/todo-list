package com.epam.todo.guid.infrastructure.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.epam.fans.guid.domain.service.IWorkerNodeService;
import com.epam.fans.guid.infrastructure.mapper.WorkerNodeMapper;
import com.epam.fans.guid.infrastructure.model.WorkerNode;
import org.springframework.stereotype.Service;

@Service
public class WorkerNodeServiceImpl extends ServiceImpl<WorkerNodeMapper, WorkerNode> implements IWorkerNodeService {

}
