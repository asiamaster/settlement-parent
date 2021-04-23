package com.dili.settlement.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 任务 定时任务执行器
 */
public class AsyncTaskExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskExecutor.class);

    /** 重试任务延迟队列 */
    private static final DelayQueue<AbstractRetryTask> RETRY_TASK_QUEUE = new DelayQueue<>();

    /** 定时任务线程池 */
    private static final ScheduledExecutorService scheduledExecutorService;

    /** 普通任务线程池 */
    private static final ExecutorService executorService;

    static {//内部使用  直接静态初始化并加入重试队列监测任务
        int coreSize = Runtime.getRuntime().availableProcessors();
        executorService = new ThreadPoolExecutor(coreSize, 100, 2L, TimeUnit.MINUTES, new ArrayBlockingQueue<>(100));
        scheduledExecutorService = new ScheduledThreadPoolExecutor(coreSize);
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                AbstractRetryTask retryTask = null;
                while((retryTask = RETRY_TASK_QUEUE.poll()) != null) {
                    executorService.submit(retryTask);
                }
            } catch (Exception e) {
                LOGGER.error("Retry queue monitor task error", e);
            }
        }, 0L, 1000L, TimeUnit.MILLISECONDS);
    }

    /**
     * 为延迟队列添加元素
     * @param retryTask
     */
    public static void putRetryTask(AbstractRetryTask retryTask) {
        RETRY_TASK_QUEUE.put(retryTask);
    }

    /**
     *
     * @param task
     * @param <T>
     * @return
     */
    public static <T> T submit(Callable<T> task) {
        try {
            Future<T> future = executorService.submit(task);
            return future.get();
        } catch (RejectedExecutionException e) {
            LOGGER.error("The task is rejected", e);
        } catch (Exception e) {
            LOGGER.error("Execute task error", e);
        }
        return null;
    }
}
