package com.dili.settlement.mapper;

import com.dili.settlement.domain.SettleFeeItem;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleFeeItemMapper extends MyMapper<SettleFeeItem> {
    /**
     * 根据结算单ID查询费用项列表
     * @param settleOrderId
     * @return
     */
    List<SettleFeeItem> listBySettleOrderId(@Param("settleOrderId") Long settleOrderId);

    /**
     * 根据结算单列表批量查询
     * @param settleOrderIdList
     * @return
     */
    List<SettleFeeItem> listBySettleOrderIdList(@Param("settleOrderIdList") List<Long> settleOrderIdList);
}