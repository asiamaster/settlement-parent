package com.dili.settlement.task;

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 重试任务抽象类
 * @param <T>
 */
public abstract class AbstractRetryTask<T> implements Delayed, Callable<T> {

    /** 延迟执行时间 毫秒*/
    private long mills;

    /** 重试总次数 */
    private int times;

    /** 间隔时间 毫秒*/
    private long interval;

    /** 当前执行次数 */
    private int currentTimes;

    public AbstractRetryTask(int times, long interval) {
        this.mills = System.currentTimeMillis();
        this.times = times;
        this.interval = interval;
    }

    public long getMills() {
        return mills;
    }

    public void setMills(long mills) {
        this.mills = mills;
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

    public int getCurrentTimes() {
        return currentTimes;
    }

    public void setCurrentTimes(int currentTimes) {
        this.currentTimes = currentTimes;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return this.mills - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed task) {
        AbstractRetryTask retryTask = (AbstractRetryTask) task;
        return this.mills - retryTask.getMills() <= 0L ? -1 : 1;
    }

    /**
     * 判断是否能进行下一次并重新赋值相关参数
     * @return
     */
    public boolean hasNext() {
        if (this.currentTimes < this.times) {
            this.currentTimes += 1;
            this.mills = System.currentTimeMillis() + (this.currentTimes * this.interval);
            return true;
        }
        return false;
    }
}
