package com.dili.settlement.service;

import com.dili.settlement.domain.SettleConfig;

import java.util.List;

/**
 * 加载结算方式service接口
 */
public interface SettleWayService {

    /**
     * 查询支付结算方式 仅用于选择项
     * @param multi
     * @return
     */
    List<SettleConfig> payChooseList(Long marketId, boolean multi);

    /**
     * 查询结算方式 仅用于组合支付表单项
     * @return
     */
    List<SettleConfig> payFormList(Long marketId);

    /**
     * 查询退款结算方式 仅用于选择项
     * @param firmId
     * @return
     */
    List<SettleConfig> refundChooseList(Long firmId);
}
