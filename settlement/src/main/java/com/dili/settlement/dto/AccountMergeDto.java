package com.dili.settlement.dto;

import com.dili.settlement.domain.CustomerAccountSerial;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户账户修改相关dto
 */
public class AccountMergeDto {
    /** 商户ID */
    private Long mchId;
    /** 客户ID */
    private Long customerId;
    /** 金额 正负值 */
    private Long amount;
    /** 冻结金额 正负值 */
    private Long frozenAmount;
    /** 流水列表 */
    private List<CustomerAccountSerial> serialList;

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public List<CustomerAccountSerial> getSerialList() {
        return serialList;
    }

    public void setSerialList(List<CustomerAccountSerial> serialList) {
        this.serialList = serialList;
    }

    /**
     * 构建对象
     * @param mchId
     * @param customerId
     * @return
     */
    public static AccountMergeDto build(Long mchId, Long customerId) {
        AccountMergeDto accountMergeDto = new AccountMergeDto();
        accountMergeDto.setMchId(mchId);
        accountMergeDto.setCustomerId(customerId);
        accountMergeDto.setAmount(0L);
        accountMergeDto.setFrozenAmount(0L);
        accountMergeDto.setSerialList(new ArrayList<>());
        return accountMergeDto;
    }

    /**
     * 增加金额
     * @param amount
     * @param serial
     */
    public void addAmount(Long amount, CustomerAccountSerial serial) {
        this.amount += amount;
        this.serialList.add(serial);
    }

    /**
     * 扣减金额
     * @param amount
     * @param serial
     */
    public void subAmount(Long amount, CustomerAccountSerial serial) {
        this.amount -= amount;
        this.serialList.add(serial);
    }

    /**
     * 增加冻结金额
     * @param frozenAmount
     */
    public void addFrozenAmount(Long frozenAmount) {
        this.frozenAmount += frozenAmount;
    }

    /**
     * 扣减冻结金额
     * @param frozenAmount
     */
    public void subFrozenAmount(Long frozenAmount) {
        this.frozenAmount -= frozenAmount;
    }
}
