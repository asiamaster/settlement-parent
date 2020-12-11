package com.dili.settlement.handler;

import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.service.RetryRecordService;
import com.dili.settlement.service.SettleOrderLinkService;
import com.dili.settlement.task.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 回调处理器
 */
@Component
public class CallbackHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackHandler.class);

    @Autowired
    private CallbackConfiguration callbackConfiguration;
    @Autowired
    private RetryRecordService retryRecordService;
    @Autowired
    private SettleOrderLinkService settleOrderLinkService;

    /**
     * 初始化各执行线程
     */
    @PostConstruct
    public void init() {
        //添加数据准备任务
        LOGGER.info("----------- start prepare task -----------");
        ExecutorService prepareExecutorService = Executors.newFixedThreadPool(callbackConfiguration.getPrepareThreads());
        for (int i = 0; i < callbackConfiguration.getPrepareThreads(); i++) {
            prepareExecutorService.submit(new PrepareQueueTask(callbackConfiguration, settleOrderLinkService));
        }
        LOGGER.info("----------- end prepare task -----------");
        //添加回调执行任务
        LOGGER.info("----------- start execute task -----------");
        ExecutorService executeExecutorService = Executors.newFixedThreadPool(callbackConfiguration.getExecuteThreads());
        for (int i = 0; i < callbackConfiguration.getExecuteThreads(); i++) {
            executeExecutorService.submit(new ExecuteQueueTask(callbackConfiguration, retryRecordService));
        }
        LOGGER.info("----------- end execute task -----------");
        //添加缓存处理任务（暂时这样处理）
        LOGGER.info("----------- start cache task -----------");
        ExecutorService cacheExecutorService = Executors.newFixedThreadPool(callbackConfiguration.getCacheThreads());
        for (int i = 0; i < callbackConfiguration.getCacheThreads(); i++) {
            cacheExecutorService.submit(new CacheQueueTask(callbackConfiguration));
        }
        LOGGER.info("----------- end cache task -----------");

        //添加守护线程 以便任务线程终止后重新提交相应任务线程
        if (callbackConfiguration.getDamonThreadEnable()) {
            ExecutorService damonExecutorService = Executors.newFixedThreadPool(3);
            damonExecutorService.submit(new PrepareDamonTask(callbackConfiguration, settleOrderLinkService, prepareExecutorService));
            damonExecutorService.submit(new ExecuteDamonTask(callbackConfiguration, retryRecordService, executeExecutorService));
            damonExecutorService.submit(new CacheDamonTask(callbackConfiguration, cacheExecutorService));
        }

    }
}
