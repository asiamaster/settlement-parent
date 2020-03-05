package com.dili.settlement.task;

import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.service.RetryErrorService;
import com.dili.settlement.service.RetryRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 回调执行守护线程
 */
public class ExecuteDamonTask extends DamonTask implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteDamonTask.class);

    private RetryRecordService retryRecordService;
    private RetryErrorService retryErrorService;

    public ExecuteDamonTask(CallbackConfiguration callbackConfiguration, RetryRecordService retryRecordService, RetryErrorService retryErrorService, ExecutorService executorService) {
        super(callbackConfiguration, executorService);
        this.retryRecordService = retryRecordService;
        this.retryErrorService = retryErrorService;
    }

    @Override
    public Boolean call() {
        while (true) {
            try {
                Thread.sleep(callbackConfiguration.getDamonThreadSleepMills());
            } catch (InterruptedException e) {
                LOGGER.error("execute damon thread sleep", e);
            }
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)executorService;
                int reboot = callbackConfiguration.getExecuteThreads() - threadPoolExecutor.getActiveCount();
                if (reboot <= 0) {
                    continue;
                }
                for (int i = 0; i < reboot; i++) {
                    executorService.submit(new ExecuteQueueTask(callbackConfiguration, retryRecordService, retryErrorService));
                }
            } catch (Exception e) {
                LOGGER.error("execute damon task error", e);
            }
        }
    }
}
