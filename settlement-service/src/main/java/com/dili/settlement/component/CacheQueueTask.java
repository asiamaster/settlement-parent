package com.dili.settlement.component;

import com.dili.settlement.dto.CallbackDto;

import java.util.concurrent.Callable;

/**
 * 用于处理执行失败数据处理
 */
public class CacheQueueTask implements Callable<Boolean> {
    @Override
    public Boolean call() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
