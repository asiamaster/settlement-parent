package com.dili.settlement.component;

import com.dili.settlement.dto.CallbackDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 用于处理执行失败数据处理
 */
public class CacheQueueTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheQueueTask.class);
    @Override
    public Boolean call() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                LOGGER.error("cache thread sleep", e);
            }
            CallbackDto callbackDto = CallbackHolder.pollCache();
            if (callbackDto == null) {
                continue;
            }
            if (callbackDto.drop()) {
                continue;
            }
            if (callbackDto.prepare()) {
                CallbackHolder.offerExecute(callbackDto);
            } else {
                CallbackHolder.offerCache(callbackDto);
            }
        }
    }
}
