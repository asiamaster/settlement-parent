package com.dili.settlement.task;

import com.dili.settlement.config.CallbackConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 执行失败守护进程
 */
public class CacheDamonTask extends DamonTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheDamonTask.class);

    public CacheDamonTask(CallbackConfiguration callbackConfiguration, ExecutorService executorService) {
        super(callbackConfiguration, executorService);
    }

    @Override
    public Boolean call()  {
        while (true) {
            try {
                Thread.sleep(callbackConfiguration.getDamonThreadSleepMills());
            } catch (InterruptedException e) {
                LOGGER.error("cache damon thread sleep", e);
            }
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)executorService;
                int reboot = callbackConfiguration.getCacheThreads() - threadPoolExecutor.getActiveCount();
                if (reboot <= 0) {
                    continue;
                }
                for (int i = 0; i < reboot; i++) {
                    executorService.submit(new CacheQueueTask(callbackConfiguration));
                }
            } catch (Exception e) {
                LOGGER.error("cache damon task error", e);
            }
        }
    }
}
