package com.dili.settlement.domain;

import com.dili.ss.domain.BaseDomain;

/**
 * 转抵明细
 */
public class TransferDetail extends BaseDomain {

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
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户手机号
     */
    private String customerPhone;

    /**
     * 客户证件号
     */
    private String customerCertificate;

    /**
     * 金额
     */
    private Long amount;

    /** 收费项ID */
    private Long chargeItemId;

    /** 收费项名称 */
    private String chargeItemName;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
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
     * 获取结算单编码
     *
     * @return settle_order_code - 结算单编码
     */
    public String getSettleOrderCode() {
        return settleOrderCode;
    }

    /**
     * 设置结算单编码
     *
     * @param settleOrderCode 结算单编码
     */
    public void setSettleOrderCode(String settleOrderCode) {
        this.settleOrderCode = settleOrderCode;
    }

    /**
     * 获取客户ID
     *
     * @return customer_id - 客户ID
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * 设置客户ID
     *
     * @param customerId 客户ID
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 获取客户姓名
     *
     * @return customer_name - 客户姓名
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置客户姓名
     *
     * @param customerName 客户姓名
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取客户手机号
     *
     * @return customer_phone - 客户手机号
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * 设置客户手机号
     *
     * @param customerPhone 客户手机号
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     * 获取客户证件号
     *
     * @return customer_certificate - 客户证件号
     */
    public String getCustomerCertificate() {
        return customerCertificate;
    }

    /**
     * 设置客户证件号
     *
     * @param customerCertificate 客户证件号
     */
    public void setCustomerCertificate(String customerCertificate) {
        this.customerCertificate = customerCertificate;
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

    public Long getChargeItemId() {
        return chargeItemId;
    }

    public void setChargeItemId(Long chargeItemId) {
        this.chargeItemId = chargeItemId;
    }

    public String getChargeItemName() {
        return chargeItemName;
    }

    public void setChargeItemName(String chargeItemName) {
        this.chargeItemName = chargeItemName;
    }
}