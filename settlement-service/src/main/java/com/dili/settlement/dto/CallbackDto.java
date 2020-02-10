package com.dili.settlement.dto;

import java.util.Map;

/**
 * 用于回调的实体
 */
public class CallbackDto {

    //次数
    private int times;
    //可执行时间(用于判断是否已到可执行时间)
    private long mills;
    private String url;
    private Map<String, String> data;

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
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
}
