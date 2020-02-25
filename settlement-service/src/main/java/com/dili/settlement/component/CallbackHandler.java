package com.dili.settlement.component;

import com.dili.settlement.service.ApplicationConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 回调处理器
 */
@Component
public class CallbackHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackHandler.class);

    //是否签名
    @Value("${settlement.callback.sign:true}")
    private boolean sign;
    @Value("${settlement.callback.sign.key:qaz@wsx}")
    private String signKey;
    //回调次数
    @Value("${settlement.callback.times:3}")
    private int times;
    //处理回调数据线程数
    @Value("${settlement.callback.prepare.threads:5}")
    private int prepareThreads;
    //回调处理线程数
    @Value("${settlement.callback.execute.threads:5}")
    private int executeThreads;
    //重新回调线程数
    @Value("${settlement.callback.cache.threads:2}")
    private int cacheThreads;
    //时间间隔 秒
    @Value("${settlement.callback.interval:30}")
    private int interval;

    @Resource
    private ApplicationConfigService applicationConfigService;

    /**
     * 初始化各执行线程
     */
    @PostConstruct
    public void init() {
        int poolSize = this.prepareThreads + this.executeThreads + this.cacheThreads;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        //添加数据准备任务
        LOGGER.info("----------- start prepare task -----------");
        for (int i = 0; i < prepareThreads; i++) {
            executorService.submit(new SourceQueueTask(sign, signKey, times, interval, applicationConfigService));
        }
        LOGGER.info("----------- end prepare task -----------");
        //添加回调执行任务
        LOGGER.info("----------- start execute task -----------");
        for (int i = 0; i < executeThreads; i++) {
            executorService.submit(new ExecuteQueueTask());
        }
        //添加缓存处理任务（暂时这样处理）
        LOGGER.info("----------- start cache task -----------");
        for (int i = 0; i < cacheThreads; i++) {
            executorService.submit(new CacheQueueTask());
        }
        LOGGER.info("----------- end cache task -----------");
    }
}
