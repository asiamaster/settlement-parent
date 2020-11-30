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
     * @param settleOrderDto
     */
    SettleOrder save(SettleOrderDto settleOrderDto);

    /**
     * 验证业务订单号是否存在
     * @return
     */
    boolean existsOrderCode(Long appId, String orderCode);

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
     * 根据id列表查询总金额
     * @param settleOrderDto
     * @return
     */
    Long queryTotalAmount(SettleOrderDto settleOrderDto);

    /**
     * 结算修改
     * @param po
     * @return
     */
    int updateSettle(SettleOrder po);
}