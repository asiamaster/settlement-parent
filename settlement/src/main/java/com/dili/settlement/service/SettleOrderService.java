package com.dili.settlement.service;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.ChargeDateDto;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleAmountDto;
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
    SettleAmountDto queryAmount(SettleOrderDto settleOrderDto);

    /**
     * 结算修改
     * @param po
     * @return
     */
    int updateSettle(SettleOrder po);

    /**
     * 将冲正单id转换为原单id
     * @param id
     * @return
     */
    Long convertReverseOrderId(Long id);

    /**
     * 根据结算单号查询结算单
     * @param code
     * @return
     */
    SettleOrder getByCode(String code);

    /**
     * 根据id列表批量查询并加锁
     * @param ids
     * @return
     */
    List<SettleOrder> lockList(List<Long> ids);

    /**
     * 批量结算修改
     * @param settleOrderList
     */
    int batchSettleUpdate(List<SettleOrder> settleOrderList, String tradeNo);

    /**
     * 根据结算单号查询结算单
     * @param appId 应用ID
     * @param orderCode 订单号
     * @return
     */
    SettleOrder get(Long appId, String orderCode);

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
     * @param orderCode
     */
    void cancel(Long appId, String orderCode);

    /**
     * 作废
     * @param param
     */
    void invalid(InvalidRequestDto param);

    /**
     * 根据ID删除
     * @param id
     * @param version
     * @return
     */
    int deleteById(Long id, Integer version);

    /**
     * 修改收款日期
     * @param chargeDateDto
     * @return
     */
    int updateChargeDate(ChargeDateDto chargeDateDto);

    /**
     * 结算回调处理
     * @param settleOrder
     * @return
     */
    boolean callback(SettleOrder settleOrder);
}