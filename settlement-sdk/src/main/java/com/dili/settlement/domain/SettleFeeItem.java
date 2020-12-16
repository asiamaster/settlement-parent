package com.dili.settlement.domain;

import com.dili.ss.domain.BaseDomain;

/**
 * 由MyBatis Generator工具自动生成
 * 结算费用项
 * This file was generated on 2020-11-26 16:30:28.
 */
public class SettleFeeItem extends BaseDomain {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 结算单ID
     */
    private Long settleOrderId;

    /**
     * 结算单编码
     */
    private String settleOrderCode;

    /**
     * 费用项ID
     */
    private Long chargeItemId;

    /**
     * 费用项名称
     */
    private String chargeItemName;

    /**
     * 费用类型
     */
    private Integer feeType;

    /**
     * 费用名称
     */
    private String feeName;

    /**
     * 金额
     */
    private Long amount;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取结算单ID
     *
     * @return settle_order_id - 结算单ID
     */
    public Long getSettleOrderId() {
        return settleOrderId;
    }

    /**
     * 设置结算单ID
     *
     * @param settleOrderId 结算单ID
     */
    public void setSettleOrderId(Long settleOrderId) {
        this.settleOrderId = settleOrderId;
    }

    /**
     * getter
     * @return
     */
    public String getSettleOrderCode() {
        return settleOrderCode;
    }

    /**
     * setter
     * @param settleOrderCode
     */
    public void setSettleOrderCode(String settleOrderCode) {
        this.settleOrderCode = settleOrderCode;
    }

    /**
     * getter
     * @return
     */
    public Long getChargeItemId() {
        return chargeItemId;
    }

    /**
     * setter
     * @param chargeItemId
     */
    public void setChargeItemId(Long chargeItemId) {
        this.chargeItemId = chargeItemId;
    }

    /**
     * getter
     * @return
     */
    public String getChargeItemName() {
        return chargeItemName;
    }

    /**
     * setter
     * @param chargeItemName
     */
    public void setChargeItemName(String chargeItemName) {
        this.chargeItemName = chargeItemName;
    }

    /**
     * 获取费用类型
     *
     * @return fee_type - 费用类型
     */
    public Integer getFeeType() {
        return feeType;
    }

    /**
     * 设置费用类型
     *
     * @param feeType 费用类型
     */
    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    /**
     * 获取费用类型名称
     *
     * @return fee_name - 费用类型名称
     */
    public String getFeeName() {
        return feeName;
    }

    /**
     * 设置费用类型名称
     *
     * @param feeName 费用类型名称
     */
    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    /**
     * 获取金额
     *
     * @return amount - 金额
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置金额
     *
     * @param amount 金额
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }
}