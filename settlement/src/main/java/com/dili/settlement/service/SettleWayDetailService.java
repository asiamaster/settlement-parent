package com.dili.settlement.service;

import com.dili.settlement.domain.SettleWayDetail;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-01 14:13:21.
 */
public interface SettleWayDetailService extends BaseService<SettleWayDetail, Long> {
    /**
     * 根据结算单ID查询明细
     * @param settleOrderId
     * @return
     */
    List<SettleWayDetail> listBySettleOrderId(Long settleOrderId);

    /**
     * 根据结算单code查询明细
     * @param settleOrderCode
     * @return
     */
    List<SettleWayDetail> listBySettleOrderCode(String settleOrderCode);
}