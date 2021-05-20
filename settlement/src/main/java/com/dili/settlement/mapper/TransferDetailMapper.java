package com.dili.settlement.mapper;

import com.dili.settlement.domain.TransferDetail;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransferDetailMapper extends MyMapper<TransferDetail> {
    /**
     * 根据结算单ID删除
     * @param settleOrderId
     * @return
     */
    int deleteBySettleOrderId(@Param("settleOrderId") Long settleOrderId);

    /**
     * 根据结算单ID查询费用项列表
     * @param settleOrderId
     * @return
     */
    List<TransferDetail> listBySettleOrderId(@Param("settleOrderId") Long settleOrderId);

    /**
     * 根据结算单列表批量查询
     * @param settleOrderIdList
     * @return
     */
    List<TransferDetail> listBySettleOrderIdList(@Param("settleOrderIdList") List<Long> settleOrderIdList);

}