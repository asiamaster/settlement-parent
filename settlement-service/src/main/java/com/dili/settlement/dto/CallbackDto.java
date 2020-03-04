package com.dili.settlement.dto;

import java.util.Map;

/**
 * 用于回调的实体
 */
public class CallbackDto {

    //当前次数
    private int currentTimes;
    //最大次数
    private int times;
    //间隔时间
    private long interval;
    //可执行时间(用于判断是否已到可执行时间)
    private long mills;
    private String url;
    private Map<String, String> data;

    //重试记录ID
    private Long retryRecordId;
    //结算单ID
    private Long businessId;
    //结算单CODE
    private String businessCode;
    /**
     * 执行失败时调用
     */
    public void failure() {
        this.currentTimes += 1;
        this.mills = System.currentTimeMillis() + (this.currentTimes * this.interval);
    }

    /**
     * 是否达到再次执行的条件
     * @return
     */
    public boolean prepare() {
        return this.currentTimes < this.times && System.currentTimeMillis() > this.mills;
    }

    /**
     * 是否达到丢弃条件
     * @return
     */
    public boolean drop() {
        return this.currentTimes >= this.times;
    }

    public int getCurrentTimes() {
        return currentTimes;
    }

    public void setCurrentTimes(int currentTimes) {
        this.currentTimes = currentTimes;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getMills() {
        return mills;
    }

    public void setMills(long mills) {
        this.mills = mills;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public Long getRetryRecordId() {
        return retryRecordId;
    }

    public void setRetryRecordId(Long retryRecordId) {
        this.retryRecordId = retryRecordId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }
}
