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
    //是否开启守护线程
    private boolean damonThreadEnable;
    //守护线程睡眠时间
    private long damonThreadSleepMills;

    /**
     * get 方法
     * @return
     */
    public boolean getSign() {
        return sign;
    }

    /**
     * set 方法
     * @param sign
     */
    public void setSign(boolean sign) {
        this.sign = sign;
    }

    /**
     * get 方法
     * @return
     */
    public String getSignKey() {
        return signKey;
    }

    /**
     * set 方法
     * @param signKey
     */
    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    /**
     * get 方法
     * @return
     */
    public int getTimes() {
        return times;
    }

    /**
     * set 方法
     * @param times
     */
    public void setTimes(int times) {
        this.times = times;
    }

    /**
     * get 方法
     * @return
     */
    public int getPrepareThreads() {
        return prepareThreads;
    }

    /**
     * set 方法
     * @param prepareThreads
     */
    public void setPrepareThreads(int prepareThreads) {
        this.prepareThreads = prepareThreads;
    }

    /**
     * get 方法
     * @return
     */
    public int getExecuteThreads() {
        return executeThreads;
    }

    /**
     * set 方法
     * @param executeThreads
     */
    public void setExecuteThreads(int executeThreads) {
        this.executeThreads = executeThreads;
    }

    /**
     * get 方法
     * @return
     */
    public int getCacheThreads() {
        return cacheThreads;
    }

    /**
     * set 方法
     * @param cacheThreads
     */
    public void setCacheThreads(int cacheThreads) {
        this.cacheThreads = cacheThreads;
    }

    /**
     * get 方法
     * @return
     */
    public long getIntervalMills() {
        return intervalMills;
    }

    /**
     * set 方法
     * @param intervalMills
     */
    public void setIntervalMills(long intervalMills) {
        this.intervalMills = intervalMills;
    }

    /**
     * get 方法
     * @return
     */
    public long getTaskThreadSleepMills() {
        return taskThreadSleepMills;
    }

    /**
     * set 方法
     * @param taskThreadSleepMills
     */
    public void setTaskThreadSleepMills(long taskThreadSleepMills) {
        this.taskThreadSleepMills = taskThreadSleepMills;
    }

    /**
     *
     * @return
     */
    public boolean getDamonThreadEnable() {
        return damonThreadEnable;
    }

    /**
     *
     * @param damonThreadEnable
     */
    public void setDamonThreadEnable(boolean damonThreadEnable) {
        this.damonThreadEnable = damonThreadEnable;
    }

    /**
     * get 方法
     * @return
     */
    public long getDamonThreadSleepMills() {
        return damonThreadSleepMills;
    }

    /**
     * set 方法
     * @param damonThreadSleepMills
     */
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
