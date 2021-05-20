package com.dili.settlement.service;

import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.dto.CustomerAccountDto;
import com.dili.settlement.dto.EarnestTransferDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.PageOutput;

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
     * 查询并锁定
     * @param id
     * @return
     */
    CustomerAccount lockGetById(Long id);

    /**
     * 创建定金账户
     * @param customerAccount
     */
    void create(CustomerAccount customerAccount);

    /**
     * 冻结
     * @param mchId
     * @param customerId
     * @param amount
     */
    void freeze(Long mchId, Long customerId, Long amount);

    /**
     * 解冻
     * @param mchId
     * @param customerId
     * @param amount
     */
    void unfreeze(Long mchId, Long customerId, Long amount);

    /**
     * 分页查询客户账户信息
     * @param query
     * @return
     */
    PageOutput<List<CustomerAccount>> listPagination(CustomerAccountDto query);

    /**
     * 转移
     * @param transferDto
     */
    void transfer(EarnestTransferDto transferDto);

    /**
     * 修改定金账户客户信息
     * @param customer
     */
    void updateCustomerInfo(CustomerExtendDto customer);

    /**
     * 冻结转抵金额
     * @param mchId
     * @param customerId
     * @param amount
     */
    void freezeTransfer(Long mchId, Long customerId, Long amount);

    /**
     * 解冻转抵金额
     * @param mchId
     * @param customerId
     * @param amount
     */
    void unfreezeTransfer(Long mchId, Long customerId, long amount);

    /**
     * 操作转抵账户及流水
     * @param mchId
     * @param customerId
     * @param amount 正负值
     * @param frozenAmount 正负值
     * @param serialList
     */
    void handleTransfer(Long mchId, Long customerId, Long amount, Long frozenAmount, List<CustomerAccountSerial> serialList);

    /**
     * 操作定金账户及流水
     * @param mchId
     * @param customerId
     * @param amount 正负值
     * @param frozenAmount 正负值
     * @param serialList
     */
    void handleEarnest(Long mchId, Long customerId, Long amount, Long frozenAmount, List<CustomerAccountSerial> serialList);
}