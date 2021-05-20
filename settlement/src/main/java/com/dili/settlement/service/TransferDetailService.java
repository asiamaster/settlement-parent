package com.dili.settlement.service;

import com.dili.settlement.domain.TransferDetail;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2021-05-18 16:15:28.
 */
public interface TransferDetailService extends BaseService<TransferDetail, Long> {

    /**
     * 根据结算单ID删除
     * @param settleOrderId
     */
    int deleteBySettleOrderId(Long settleOrderId);

    /**
     * 根据结算单ID查询转抵列表
     * @param settleOrderId
     * @return
     */
    List<TransferDetail> listBySettleOrderId(Long settleOrderId);

    /**
     * 根据结算单ID列表查询转抵信息
     * @param settleOrderIdList
     * @return
     */
    List<TransferDetail> listBySettleOrderIdList(List<Long> settleOrderIdList);
}