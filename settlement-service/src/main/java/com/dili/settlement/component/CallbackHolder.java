package com.dili.settlement.component;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.CallbackDto;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 用于持有待回调数据
 */
public class CallbackHolder {

    //用于存放原始数据
    private static final ConcurrentLinkedQueue<SettleOrder> source = new ConcurrentLinkedQueue<>();
    //用于存放执行数据
    private static final ConcurrentLinkedQueue<CallbackDto> execute = new ConcurrentLinkedQueue<>();
    //用于存放执行失败数据
    private static final ConcurrentLinkedQueue<CallbackDto> cache = new ConcurrentLinkedQueue<>();

    /**
     * 放入原始数据
     * @param settleOrder
     */
    public static boolean offerSource(SettleOrder settleOrder) {
        return source.offer(settleOrder);
    }

    /**
     * 取出原始数据
     * @return
     */
    public static SettleOrder pollSource() {
        return source.poll();
    }

    /**
     * 放入执行数据
     * @param callbackDto
     * @return
     */
    public static boolean offerExecute(CallbackDto callbackDto) {
        return execute.offer(callbackDto);
    }

    /**
     * 取出执行数据
     * @return
     */
    public static CallbackDto pollExecute() {
        return execute.poll();
    }

    /**
     * 缓存执行失败数据
     * @param callbackDto
     * @return
     */
    public static boolean offerCache(CallbackDto callbackDto) {
        return cache.offer(callbackDto);
    }

    /**
     * 取出缓存数据
     * @return
     */
    public static CallbackDto pollCache() {
        return cache.poll();
    }
}
