package com.dili.settlement.service;

import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-12-01 16:37:13.
 */
public interface CustomerAccountService extends BaseService<CustomerAccount, Long> {

    /**
     * 查询并锁定
     * @param mchId
     * @param customerId
     * @return
     */
    CustomerAccount lockGet(Long mchId, Long customerId);

    /**
     * 查询
     * @param mchId
     * @param customerId
     * @return
     */
    CustomerAccount getBy(Long mchId, Long customerId);
    /**
     * 创建定金账户
     * @param customerAccount
     */
    void create(CustomerAccount customerAccount);

    /**
     * 操作账户及流水 支付
     * @param mchId
     * @param customerId
     * @param amount 有正负值
     * @param accountSerialList
     */
    void handlePay(Long mchId, Long customerId, Long amount, List<CustomerAccountSerial> accountSerialList);

    /**
     * 操作账户及流水 退款
     * @param mchId
     * @param customerId
     * @param amount 负值
     * @param accountSerialList
     */
    void handleRefund(Long mchId, Long customerId, Long amount, List<CustomerAccountSerial> accountSerialList);
}