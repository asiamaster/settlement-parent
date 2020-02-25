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
    boolean existsBusinessCode(Long appId, String businessCode);

    /**
     * 根据结算单id取消
     * @param id
     */
    void cancelById(Long id);

    /**
     * 根据结算单号取消
     * @param code
     */
    void cancelByCode(String code);

    /**
     * 根据应用id、业务单号取消
     * @param appId
     * @param businessCode
     */
    void cancel(Long appId, String businessCode);

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

    /**
     * 根据结算单号查询结算单
     * @param appId 应用ID
     * @param businessCode 业务单号
     * @return
     */
    SettleOrder get(Long appId, String businessCode);

    /**
     * 实现缴费处理逻辑  更改状态、增加金额
     * @param po
     */
    void pay(SettleOrder po, SettleOrderDto settleOrderDto);
    /**
     * 实现退款处理逻辑  更改状态、扣减金额
     * @param po
     */
    void refund(SettleOrder po, SettleOrderDto settleOrderDto);

    /**
     * 根据id列表查询总金额
     * @param settleOrderDto
     * @return
     */
    Long queryTotalAmount(SettleOrderDto settleOrderDto);

}