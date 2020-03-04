package com.dili.settlement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 回调相关参数配置
 */
@Component
@ConfigurationProperties(prefix = "settlement.callback")
public class CallbackConfiguration {

    //是否签名
    private boolean sign;
    //签名key
    private String signKey;
    //回调次数
    private int times;
    //数据准备线程数
    private int prepareThreads;
    //回调执行线程数
    private int executeThreads;
    //重新回调线程数
    private int cacheThreads;
    //回调间隔 毫秒
    private long intervalMills;
    //任务线程睡眠时间 毫秒
    private long taskThreadSleepMills;
    //守护线程睡眠时间
    private long damonThreadSleepMills;

    public boolean getSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getPrepareThreads() {
        return prepareThreads;
    }

    public void setPrepareThreads(int prepareThreads) {
        this.prepareThreads = prepareThreads;
    }

    public int getExecuteThreads() {
        return executeThreads;
    }

    public void setExecuteThreads(int executeThreads) {
        this.executeThreads = executeThreads;
    }

    public int getCacheThreads() {
        return cacheThreads;
    }

    public void setCacheThreads(int cacheThreads) {
        this.cacheThreads = cacheThreads;
    }

    public long getIntervalMills() {
        return intervalMills;
    }

    public void setIntervalMills(long intervalMills) {
        this.intervalMills = intervalMills;
    }

    public long getTaskThreadSleepMills() {
        return taskThreadSleepMills;
    }

    public void setTaskThreadSleepMills(long taskThreadSleepMills) {
        this.taskThreadSleepMills = taskThreadSleepMills;
    }

    public long getDamonThreadSleepMills() {
        return damonThreadSleepMills;
    }

    public void setDamonThreadSleepMills(long damonThreadSleepMills) {
        this.damonThreadSleepMills = damonThreadSleepMills;
    }

    /**
     * 获取总线程数
     * @return
     */
    public int countTotalThreads() {
        return this.prepareThreads + this.executeThreads + this.cacheThreads;
    }
}
