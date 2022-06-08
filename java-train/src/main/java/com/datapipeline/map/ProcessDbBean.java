package com.datapipeline.map;

import java.util.Date;

public class ProcessDbBean {
    private Integer validatorId;
    private Integer mappingId;
    private Date batchTime;
    private Long srcTotal;
    private Long sinkTotal;
    private Long batchSize;
    private Long totalTime;

    public ProcessDbBean(Integer validatorId, Integer mappingId, Date batchTime, Long srcTotal, Long sinkTotal, Long batchSize,Long totalTime) {
        this.validatorId = validatorId;
        this.mappingId = mappingId;
        this.batchTime = batchTime;
        this.srcTotal = srcTotal;
        this.sinkTotal = sinkTotal;
        this.batchSize = batchSize;
        this.totalTime = totalTime;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getValidatorId() {
        return validatorId;
    }

    public void setValidatorId(Integer validatorId) {
        this.validatorId = validatorId;
    }

    public Integer getMappingId() {
        return mappingId;
    }

    public void setMappingId(Integer mappingId) {
        this.mappingId = mappingId;
    }

    public Date getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(Date batchTime) {
        this.batchTime = batchTime;
    }

    public Long getSrcTotal() {
        return srcTotal;
    }

    public void setSrcTotal(Long srcTotal) {
        this.srcTotal = srcTotal;
    }

    public Long getSinkTotal() {
        return sinkTotal;
    }

    public void setSinkTotal(Long sinkTotal) {
        this.sinkTotal = sinkTotal;
    }

    public Long getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Long batchSize) {
        this.batchSize = batchSize;
    }
}
