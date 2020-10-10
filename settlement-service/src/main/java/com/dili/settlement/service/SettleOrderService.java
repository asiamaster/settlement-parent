package com.dili.settlement.service;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.PageOutput;

import java.util.List;
import java.util.Map;

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
    boolean existsBusinessCode(Long appId, String orderCode);

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
     * @param orderCode 订单号
     * @return
     */
    SettleOrder get(Long appId, String orderCode);

    /**
     * 根据id列表查询总金额
     * @param settleOrderDto
     * @return
     */
    Long queryTotalAmount(SettleOrderDto settleOrderDto);

    /**
     * 用于改数据接口-批量新增已处理单据
     * @param itemList
     */
    void batchSaveDealt(List<SettleOrder> itemList);

    /**
     * 用于改数据接口-批量新增已处理、删除单据
     * @param itemList
     */
    void batchSaveDealtAndDelete(List<SettleOrder> itemList);

    /**
     * 用于改数据接口-批量修改金额
     * @param itemList
     */
    void batchUpdateAmount(List<Map<String, Object>> itemList);

    /**
     * 结算修改
     * @param po
     * @return
     */
    int updateSettle(SettleOrder po);
}