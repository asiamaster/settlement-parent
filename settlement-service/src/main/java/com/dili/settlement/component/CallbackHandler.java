package com.dili.settlement.component;

import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.service.ApplicationConfigService;
import com.dili.settlement.service.RetryErrorService;
import com.dili.settlement.service.RetryRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 回调处理器
 */
@Component
public class CallbackHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackHandler.class);

    @Resource
    private CallbackConfiguration callbackConfiguration;
    @Resource
    private ApplicationConfigService applicationConfigService;
    @Resource
    private RetryRecordService retryRecordService;
    @Resource
    private RetryErrorService retryErrorService;

    /**
     * 初始化各执行线程
     */
    @PostConstruct
    public void init() {
        int poolSize = callbackConfiguration.countTotalThreads();
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        //添加数据准备任务
        LOGGER.info("----------- start prepare task -----------");
        for (int i = 0; i < callbackConfiguration.getPrepareThreads(); i++) {
            executorService.submit(new SourceQueueTask(i, callbackConfiguration, applicationConfigService));
        }
        LOGGER.info("----------- end prepare task -----------");
        //添加回调执行任务
        LOGGER.info("----------- start execute task -----------");
        for (int i = 0; i < callbackConfiguration.getExecuteThreads(); i++) {
            executorService.submit(new ExecuteQueueTask(i, callbackConfiguration, retryRecordService, retryErrorService));
        }
        //添加缓存处理任务（暂时这样处理）
        LOGGER.info("----------- start cache task -----------");
        for (int i = 0; i < callbackConfiguration.getCacheThreads(); i++) {
            executorService.submit(new CacheQueueTask(i, callbackConfiguration));
        }
        LOGGER.info("----------- end cache task -----------");

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)executorService;
        threadPoolExecutor.getQueue();
    }
}
