package com.epam.todo.guid.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.epam.todo.guid.model.WorkerNode;
import com.epam.todo.guid.repository.WorkerNodeRepository;
import com.epam.todo.guid.service.IUidGenerateService;
import com.xfvape.uid.UidGenerator;
import com.xfvape.uid.exception.UidGenerateException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UidGenerateServiceImpl implements IUidGenerateService {

    @Resource
    private UidGenerator uidGenerator;

    @Resource
    private WorkerNodeRepository workerNodeRepository;


    @Override
    public String parseUid(Long uid) {
        return uidGenerator.parseUID(uid);
    }

    @Override
    public WorkerNode getWorkerNode(Long uid) {
        String parseStr = uidGenerator.parseUID(uid);
        JSONObject parseObj = JSONObject.parseObject(parseStr);
        return workerNodeRepository.getById(parseObj.getLong(WORKER_ID));
    }

    @Override
    public Long getUid() throws UidGenerateException {
        return uidGenerator.getUID();
    }

    @Override
    public List<Long> batchIds(Integer num) throws UidGenerateException {
        List<Long> ids = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            ids.add(uidGenerator.getUID());
        }
        return ids;
    }
}
