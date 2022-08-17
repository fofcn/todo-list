package com.epam.todo.guid.config;

import com.xfvape.uid.impl.CachedUidGenerator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties({UidGeneratorProperties.class})
public class UidGeneratorConfig {

    @Resource
    private UidGeneratorProperties uidGeneratorProperties;

    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Bean
    public CachedUidGenerator cachedUidGenerator(DisposableWorkerIdAssigner disposableWorkerIdAssigner) {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        // 对于并发数要求不高、期望长期使用的应用, 可增加```timeBits```位数, 减少```seqBits```位数.
        // 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为12次/天,
        // 那么配置成```{"workerBits":23,"timeBits":31,"seqBits":9}```时, 可支持28个节点以整体并发量14400 UID/s的速度持续运行68年.

        // 对于节点重启频率频繁、期望长期使用的应用, 可增加```workerBits```和```timeBits```位数, 减少```seqBits```位数.
        // 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为24*12次/天,
        // 那么配置成```{"workerBits":27,"timeBits":30,"seqBits":6}```时, 可支持37个节点以整体并发量2400 UID/s的速度持续运行34年.

        // 以下为可选配置, 如未指定将采用默认值
        cachedUidGenerator.setTimeBits(uidGeneratorProperties.getTimeBits());
        cachedUidGenerator.setWorkerBits(uidGeneratorProperties.getWorkerBits());
        cachedUidGenerator.setSeqBits(uidGeneratorProperties.getSeqBits());
        cachedUidGenerator.setEpochStr(uidGeneratorProperties.getEpochStr());

        // RingBuffer size扩容参数, 可提高UID生成的吞吐量
        // 默认:3， 原bufferSize=8192, 扩容后bufferSize= 8192 << 3 = 65536
        cachedUidGenerator.setBoostPower(uidGeneratorProperties.getBoostPower());
        // 指定何时向RingBuffer中填充UID, 取值为百分比(0, 100), 默认为50
        // 举例: bufferSize=1024, paddingFactor=50 -> threshold=1024 * 50 / 100 = 512.
        // 当环上可用UID数量 < 512时, 将自动对RingBuffer进行填充补全
        // <property name="paddingFactor" value="50"></property>
        cachedUidGenerator.setPaddingFactor(uidGeneratorProperties.getPaddingFactor());

        // 另外一种RingBuffer填充时机, 在Schedule线程中, 周期性检查填充
        // 默认:不配置此项, 即不实用Schedule线程. 如需使用, 请指定Schedule线程时间间隔, 单位:秒
        // cachedUidGenerator.setScheduleInterval(60L);

        // 拒绝策略: 当环已满, 无法继续填充时
        // 默认无需指定, 将丢弃Put操作, 仅日志记录. 如有特殊需求, 请实现RejectedPutBufferHandler接口(支持Lambda表达式)
        // <property name="rejectedPutBufferHandler" ref="XxxxYourPutRejectPolicy"></property>
        // cachedUidGenerator.setRejectedPutBufferHandler();
        // 拒绝策略: 当环已空, 无法继续获取时 -->
        // 默认无需指定, 将记录日志, 并抛出UidGenerateException异常. 如有特殊需求, 请实现RejectedTakeBufferHandler接口(支持Lambda表达式) -->
        // <property name="rejectedTakeBufferHandler" ref="XxxxYourTakeRejectPolicy"></property>

        return cachedUidGenerator;
    }
}

