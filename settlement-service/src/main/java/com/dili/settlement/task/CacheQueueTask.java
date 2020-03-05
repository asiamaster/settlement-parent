package com.dili.settlement.task;

import com.dili.settlement.component.CallbackHolder;
import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.dto.CallbackDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 用于处理执行失败数据处理
 */
public class CacheQueueTask extends QueueTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheQueueTask.class);

    public CacheQueueTask(CallbackConfiguration callbackConfiguration) {
        super(callbackConfiguration);
    }

    @Override
    public Boolean call() {
        while (true) {
            CallbackDto callbackDto = CallbackHolder.pollCache();
            if (callbackDto == null) {
                try {
                    Thread.sleep(callbackConfiguration.getTaskThreadSleepMills());
                } catch (InterruptedException e) {
                    LOGGER.error("cache thread sleep", e);
                }
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
