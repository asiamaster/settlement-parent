package com.dili.settlement.mapper;

import com.dili.settlement.domain.SettleWayDetail;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SettleWayDetailMapper extends MyMapper<SettleWayDetail> {

    /**
     * 根据结算单ID查询
     * @param settleOrderId
     * @return
     */
    List<SettleWayDetail> listBySettleOrderId(@Param("settleOrderId") Long settleOrderId);

    /**
     * 根据结算单code查询
     * @param settleOrderCode
     * @return
     */
    List<SettleWayDetail> listBySettleOrderCode(@Param("settleOrderCode") String settleOrderCode);
}