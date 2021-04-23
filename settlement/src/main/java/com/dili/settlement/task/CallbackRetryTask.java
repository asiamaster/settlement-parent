package com.dili.settlement.task;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.service.SettleOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 结算回调任务
 */
public class CallbackRetryTask extends AbstractRetryTask<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackRetryTask.class);

    private SettleOrderService settleOrderService;

    private SettleOrder settleOrder;

    public CallbackRetryTask(int times, long interval, SettleOrderService settleOrderService, SettleOrder settleOrder) {
        super(times, interval);
        this.settleOrderService = settleOrderService;
        this.settleOrder = settleOrder;
    }

    @Override
    public Boolean call() {
        System.out.println(Thread.currentThread().getName() + " " + this.getCurrentTimes());
        try {
            if (this.hasNext() && !settleOrderService.callback(settleOrder)) {
                AsyncTaskExecutor.putRetryTask(this);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("Execute callback task error", e);
        }
        return false;
    }
}
