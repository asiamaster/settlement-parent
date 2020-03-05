package com.dili.settlement.task;

import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.service.ApplicationConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 数据准备守护线程
 */
public class PrepareDamonTask extends DamonTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrepareDamonTask.class);

    private ApplicationConfigService applicationConfigService;

    public PrepareDamonTask(CallbackConfiguration callbackConfiguration, ApplicationConfigService applicationConfigService, ExecutorService executorService) {
        super(callbackConfiguration, executorService);
        this.applicationConfigService = applicationConfigService;
    }

    @Override
    public Boolean call() {
        while (true) {
            try {
                Thread.sleep(callbackConfiguration.getDamonThreadSleepMills());
            } catch (InterruptedException e) {
                LOGGER.error("prepare damon thread sleep", e);
            }
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
                int reboot = callbackConfiguration.getPrepareThreads() - threadPoolExecutor.getActiveCount();
                if (reboot <= 0) {
                    continue;
                }
                for (int i = 0; i < reboot; i++) {
                    executorService.submit(new PrepareQueueTask(callbackConfiguration, applicationConfigService));
                }
            } catch (Exception e) {
                LOGGER.error("prepare damon task error", e);
            }
        }
    }
}
