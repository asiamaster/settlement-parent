package com.dili.settlement.component;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.CallbackDto;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 用于持有待回调数据
 */
public class CallbackTaskHolder {

    //用于存放原始数据
    private static final ConcurrentLinkedDeque<SettleOrder> source = new ConcurrentLinkedDeque<>();
    //用于存放执行数据
    private static final ConcurrentLinkedDeque<CallbackDto> execute = new ConcurrentLinkedDeque<>();
    //用于存放执行失败数据
    private static final ConcurrentLinkedDeque<CallbackDto> cache = new ConcurrentLinkedDeque<>();
}
