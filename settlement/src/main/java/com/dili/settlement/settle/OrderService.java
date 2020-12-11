package com.dili.settlement.settle;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;

/**
 * 结算单相关操作service接口
 */
public interface OrderService {

    /**
     * 保存
     * @param settleOrderDto
     * @return
     */
    SettleOrder save(SettleOrderDto settleOrderDto);

    /**
     * 保存 个性化处理
     * @param settleOrder
     * @param settleOrderDto
     * @return
     */
    void saveSpecial(SettleOrder settleOrder, SettleOrderDto settleOrderDto);

    /**
     * 提交撤回
     * @param settleOrder
     */
    void cancel(SettleOrder settleOrder);

    /**
     * 提交撤回 个性化处理
     * @param settleOrder
     */
    void cancelSpecial(SettleOrder settleOrder);

    /**
     * 支持的结算单类型
     * @return
     */
    Integer supportType();
}
