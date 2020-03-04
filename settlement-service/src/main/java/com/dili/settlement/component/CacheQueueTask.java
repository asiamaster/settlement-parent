package com.dili.settlement.component;

import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.dto.CallbackDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 用于处理执行失败数据处理
 */
public class CacheQueueTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheQueueTask.class);

    private String threadKey;
    private int threadId;
    private CallbackConfiguration callbackConfiguration;

    public CacheQueueTask(int threadId, CallbackConfiguration callbackConfiguration) {
        this.threadId = threadId;
        this.callbackConfiguration = callbackConfiguration;
        this.threadKey = "cache-" + this.threadId;
    }

    @Override
    public Boolean call() {
        while (true) {
            try {
                Thread.sleep(callbackConfiguration.getTaskThreadSleepMills());
            } catch (InterruptedException e) {
                LOGGER.error("cache thread sleep", e);
            }
            CallbackDto callbackDto = CallbackHolder.pollCache();
            if (callbackDto == null) {
                continue;
            }
            try {
                if (callbackDto.drop()) {
                    continue;
                }
                if (callbackDto.prepare()) {
                    CallbackHolder.offerExecute(callbackDto);
                } else {
                    CallbackHolder.offerCache(callbackDto);
                }
            } catch (Exception e) {
                LOGGER.error("cache thread error", e);
            }
        }
    }
}
