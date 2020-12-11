package com.dili.settlement.mapper;

import com.dili.settlement.domain.SettleOrderLink;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface SettleOrderLinkMapper extends MyMapper<SettleOrderLink> {

    /**
     * 根据结算单ID删除
     * @param settleOrderId
     * @return
     */
    int deleteBySettleOrderId(@Param("settleOrderId") Long settleOrderId);
}