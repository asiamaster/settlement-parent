package com.dili.settlement.service;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-05 16:39:11.
 */
public interface SettleOrderService extends BaseService<SettleOrder, Long> {
    /**
     * 保存结算方法
     * @param settleOrder
     */
    void save(SettleOrder settleOrder);

    /**
     * 验证业务编号是否存在
     * @return
     */
    boolean existsBusinessCode(Long marketId, Long appId, String businessCode);

    /**
     * 根据结算单号取消
     * @param code
     */
    void cancelByCode(String code);

    /**
     * 查询结算单列表
     * @param query
     * @return
     */
    List<SettleOrder> list(SettleOrderDto query);

    /**
     * 分页查询结算单列表
     * @param query
     * @return
     */
    PageOutput<List<SettleOrder>> listPagination(SettleOrderDto query);

    /**
     * 根据结算单号查询结算单
     * @param code
     * @return
     */
    SettleOrder getByCode(String code);
}