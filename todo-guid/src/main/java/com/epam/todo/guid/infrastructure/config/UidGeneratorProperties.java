package com.epam.todo.guid.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fans.guid")
public class UidGeneratorProperties {

    /**
     * 时间比特位
     */
    private Integer timeBits = 30;

    /**
     * 机器比特位
     */
    private Integer workerBits = 18;

    /**
     * 序列比特位
     */
    private Integer seqBits = 15;

    /**
     * 相对时间基点
     */
    private String epochStr = "2016-09-20";

    /**
     * RingBuffer Size扩容参数
     */
    private Integer boostPower = 3;

    /**
     * 指定何时向RingBuffer中填充UID
     */
    private Integer paddingFactor = 50;


    public Integer getTimeBits() {
        return timeBits;
    }

    public void setTimeBits(Integer timeBits) {
        this.timeBits = timeBits;
    }

    public Integer getWorkerBits() {
        return workerBits;
    }

    public void setWorkerBits(Integer workerBits) {
        this.workerBits = workerBits;
    }

    public Integer getSeqBits() {
        return seqBits;
    }

    public void setSeqBits(Integer seqBits) {
        this.seqBits = seqBits;
    }

    public String getEpochStr() {
        return epochStr;
    }

    public void setEpochStr(String epochStr) {
        this.epochStr = epochStr;
    }

    public Integer getBoostPower() {
        return boostPower;
    }

    public void setBoostPower(Integer boostPower) {
        this.boostPower = boostPower;
    }

    public Integer getPaddingFactor() {
        return paddingFactor;
    }

    public void setPaddingFactor(Integer paddingFactor) {
        this.paddingFactor = paddingFactor;
    }
}

