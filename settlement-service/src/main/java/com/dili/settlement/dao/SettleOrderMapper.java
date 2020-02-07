package com.dili.settlement.dao;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.ss.base.MyMapper;

import java.util.List;
import java.util.Map;

public interface SettleOrderMapper extends MyMapper<SettleOrder> {
    /**
     * 根据条件查询结算单列表
     * @param query
     * @return
     */
    List<SettleOrder> list(SettleOrderDto query);

    /**
     * 根据id修改结算单状态
     * @param settleOrder
     * @return
     */
    int updateState(SettleOrder settleOrder);

    /**
     * 根据结算单号查询结算单
     * @param code
     * @return
     */
    SettleOrder getByCode(String code);

    /**
     * 结算修改
     * @param po
     * @return
     */
    int updateSettle(SettleOrder po);
}