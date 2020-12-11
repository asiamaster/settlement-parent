package com.dili.settlement.service;

import com.dili.settlement.domain.SettleFeeItem;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-11-26 16:30:28.
 */
public interface SettleFeeItemService extends BaseService<SettleFeeItem, Long> {

    /**
     * 根据结算单ID查询费用项列表
     * @param settleOrderId
     * @return
     */
    List<SettleFeeItem> listBySettleOrderId(Long settleOrderId);

    /**
     * 根据结算单列表批量查询
     * @param settleOrderIdList
     * @return
     */
    List<SettleFeeItem> listBySettleOrderIdList(List<Long> settleOrderIdList);

    /**
     * 根据结算单ID删除
     * @param settleOrderId
     * @return
     */
    int deleteBySettleOrderId(Long settleOrderId);
}