package com.epam.todo.guid.adapter.web;

import com.epam.common.core.dto.MultiResponse;
import com.epam.common.core.dto.SingleResponse;
import com.epam.todo.guid.domain.IUidGenerateService;
import com.epam.todo.guid.infrastructure.model.WorkerNode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/guid")
public class GuidController {

    @Resource
    private IUidGenerateService uidGenerateService;


    /**
     * 获取Uid
     */
    @GetMapping("/uid")
    public SingleResponse<Long> getUid() {
        return SingleResponse.of(uidGenerateService.getUid());
    }

    /**
     * 批量获取Uid
     */
    @GetMapping("/uids")
    public MultiResponse<Long> batchIds(@RequestParam Integer num) {
        return MultiResponse.of(uidGenerateService.batchIds(num));
    }

    /**
     * 解析Uid
     */
    @GetMapping("/{uid}/parse")
    public SingleResponse<String> parseUid(@PathVariable Long uid) {
        return SingleResponse.of(uidGenerateService.parseUid(uid));
    }

    /**
     * 获取WorkerNode
     */
    @GetMapping("/{uid}/worker-node")
    public SingleResponse<WorkerNode> getWorkerNode(@PathVariable Long uid) {
        return SingleResponse.of(uidGenerateService.getWorkerNode(uid));
    }
}
