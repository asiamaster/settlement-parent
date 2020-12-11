package com.dili.settlement.component;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.CallbackDto;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 用于持有待回调数据
 */
public class CallbackHolder {

    //用于存放原始数据
    private static final ConcurrentLinkedQueue<SettleOrder> SOURCE = new ConcurrentLinkedQueue<>();
    //用于存放执行数据
    private static final ConcurrentLinkedQueue<CallbackDto> EXECUTE = new ConcurrentLinkedQueue<>();
    //用于存放执行失败数据
    private static final ConcurrentLinkedQueue<CallbackDto> CACHE = new ConcurrentLinkedQueue<>();

    /**
     * 放入原始数据
     * @param settleOrder
     */
    public static boolean offerSource(SettleOrder settleOrder) {
        return SOURCE.offer(settleOrder);
    }

    /**
     * 取出原始数据
     * @return
     */
    public static SettleOrder pollSource() {
        return SOURCE.poll();
    }

    /**
     * 放入执行数据
     * @param callbackDto
     * @return
     */
    public static boolean offerExecute(CallbackDto callbackDto) {
        return EXECUTE.offer(callbackDto);
    }

    /**
     * 取出执行数据
     * @return
     */
    public static CallbackDto pollExecute() {
        return EXECUTE.poll();
    }

    /**
     * 缓存执行失败数据
     * @param callbackDto
     * @return
     */
    public static boolean offerCache(CallbackDto callbackDto) {
        return CACHE.offer(callbackDto);
    }

    /**
     * 取出缓存数据
     * @return
     */
    public static CallbackDto pollCache() {
        return CACHE.poll();
    }
}
