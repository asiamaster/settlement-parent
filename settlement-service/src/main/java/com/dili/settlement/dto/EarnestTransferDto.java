package com.dili.settlement.dto;

/**
 * 定金转移dto
 */
public class EarnestTransferDto {

    /** 转出账户ID */
    private Long accountId;

    /** 转入客户ID*/
    private Long customerId;
    /** 转入客户姓名*/
    private String customerName;
    /** 转入客户手机号*/
    private String customerPhone;
    /** 转入客户证件号*/
    private String customerCertificate;
    /** 转移金额*/
    private Long amount;
    /** 关联转移单号 */
    private String relationCode;
    /** 操作员ID*/
    private Long operatorId;
    /** 操作员姓名*/
    private String operatorName;
    /** 备注*/
    private String notes;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerCertificate() {
        return customerCertificate;
    }

    public void setCustomerCertificate(String customerCertificate) {
        this.customerCertificate = customerCertificate;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getRelationCode() {
        return relationCode;
    }

    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
