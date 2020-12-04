package com.dili.settlement.dto;

import com.dili.settlement.domain.CustomerAccountSerial;

/**
 * 客户账户流水相关dto
 */
public class CustomerAccountSerialDto extends CustomerAccountSerial {

    //市场ID
    private Long marketId;
    //商户ID
    private Long mchId;
    //客户ID
    private String customerId;
    //客户姓名
    private String customerName;
    //客户电话
    private String customerPhone;
    //客户证件号
    private String customerCertificate;

    //开始时间
    private String operateTimeStart;
    //结束时间
    private String operateTimeEnd;
    //客户名称模糊匹配
    private String customerNameMatch;
    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
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

    public String getOperateTimeStart() {
        return operateTimeStart;
    }

    public void setOperateTimeStart(String operateTimeStart) {
        this.operateTimeStart = operateTimeStart;
    }

    public String getOperateTimeEnd() {
        return operateTimeEnd;
    }

    public void setOperateTimeEnd(String operateTimeEnd) {
        this.operateTimeEnd = operateTimeEnd;
    }

    public String getCustomerNameMatch() {
        return customerNameMatch;
    }

    public void setCustomerNameMatch(String customerNameMatch) {
        this.customerNameMatch = customerNameMatch;
    }
}
