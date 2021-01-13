package com.dili.settlement.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-02-05 16:39:11.
 */
public class SettleOrder extends BaseDomain {
    private Long id;

    //市场ID
    private Long marketId;

    //市场编码
    private String marketCode;

    //商户ID
    private Long mchId;

    //商户名称
    private String mchName;

    //应用ID
    private Long appId;

    //结算编号
    private String code;

    //订单号  唯一
    private String orderCode;

    //业务编号 具体业务单号
    private String businessCode;

    //业务类型
    private String businessType;

    //业务部门id
    private Long businessDepId;

    //业务部门名称
    private String businessDepName;

    //客户ID
    private Long customerId;

    //客户姓名
    private String customerName;

    //客户手机号
    private String customerPhone;

    //客户证件号
    private String customerCertificate;

    //金额
    private Long amount;

    //抵扣金额
    private Long deductAmount;

    //提交人ID
    private Long submitterId;

    //提交人姓名
    private String submitterName;

    //提交人部门ID
    private Long submitterDepId;

    //提交人部门名称
    private String submitterDepName;

    //提交时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    //结算类型 枚举 SettleTypeEnum
    private Integer type;

    //结算方式 枚举 SettleWayEnum
    private Integer way;

    //结算状态 枚举 SettleStateEnum
    private Integer state;

    //结算员ID
    private Long operatorId;

    //结算员姓名
    @Column(name = "`operator_name`")
    private String operatorName;

    //结算时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    //银行卡号
    private String accountNumber;

    //银行名称
    private String bankName;

    //银行卡主
    private String bankCardHolder;

    //流水号
    private String serialNumber;

    //备注
    private String notes;

    private Integer version;

    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate chargeDate;

    /** 交易资金账户ID */
    private Long tradeFundAccountId;

    /** 交易卡账户ID */
    private Long tradeAccountId;

    /** 交易卡客户ID */
    private Long tradeCustomerId;

    /** 交易卡客户姓名 */
    private String tradeCustomerName;
    /**
     * 交易客户编号
     */
    private String tradeCustomerCode;

    /** 交易卡号 */
    private String tradeCardNo;

    /** 交易流水号 */
    private String tradeNo;

    /** 是否冲正 0-否 1-是*/
    private Integer reverse;

    /** 是否可抵扣 0-否 1-是*/
    private Integer deductEnable;

    /** 挂号(沈阳特有)*/
    private String trailerNumber;

    /** 持卡人姓名 */
    private String holdName;
    /** 持卡人证件号 */
    private String holdCertificateNumber;
    /** 持卡人联系电话 */
    private String holdContactsPhone;

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
     * @return market_id
     */
    public Long getMarketId() {
        return marketId;
    }

    /**
     * @param marketId
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * getter
     * @return
     */
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * setter
     * @param marketCode
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    /**
     * getter
     * @return
     */
    public Long getMchId() {
        return mchId;
    }

    /**
     * setter
     * @param mchId
     */
    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    /**
     * getter
     * @return
     */
    public String getMchName() {
        return mchName;
    }

    /**
     * setter
     * @param mchName
     */
    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    /**
     * @return app_id
     */
    public Long getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(Long appId) {
        this.appId = appId;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     *
     * @param orderCode
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * @return business_code
     */
    public String getBusinessCode() {
        return businessCode;
    }

    /**
     * @param businessCode
     */
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    /**
     * @return business_type
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * @param businessType
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * @return business_dep_id
     */
    public Long getBusinessDepId() {
        return businessDepId;
    }

    /**
     * @param businessDepId
     */
    public void setBusinessDepId(Long businessDepId) {
        this.businessDepId = businessDepId;
    }

    /**
     * @return business_dep_name
     */
    public String getBusinessDepName() {
        return businessDepName;
    }

    /**
     * @param businessDepName
     */
    public void setBusinessDepName(String businessDepName) {
        this.businessDepName = businessDepName;
    }

    /**
     * @return customer_id
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return customer_name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return customer_phone
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * @param customerPhone
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     * getter
     * @return
     */
    public String getCustomerCertificate() {
        return customerCertificate;
    }

    /**
     * setter
     * @param customerCertificate
     */
    public void setCustomerCertificate(String customerCertificate) {
        this.customerCertificate = customerCertificate;
    }

    /**
     * @return amount
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * getter
     * @return
     */
    public Long getDeductAmount() {
        return deductAmount;
    }

    /**
     * setter
     * @param deductAmount
     */
    public void setDeductAmount(Long deductAmount) {
        this.deductAmount = deductAmount;
    }

    /**
     * @return submitter_id
     */
    public Long getSubmitterId() {
        return submitterId;
    }

    /**
     * @param submitterId
     */
    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
    }

    /**
     * @return submitter_name
     */
    public String getSubmitterName() {
        return submitterName;
    }

    /**
     * @param submitterName
     */
    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    /**
     * @return submitter_dep_id
     */
    public Long getSubmitterDepId() {
        return submitterDepId;
    }

    /**
     * @param submitterDepId
     */
    public void setSubmitterDepId(Long submitterDepId) {
        this.submitterDepId = submitterDepId;
    }

    /**
     * @return submitter_dep_name
     */
    public String getSubmitterDepName() {
        return submitterDepName;
    }

    /**
     * @param submitterDepName
     */
    public void setSubmitterDepName(String submitterDepName) {
        this.submitterDepName = submitterDepName;
    }

    /**
     * @return submit_time
     */
    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    /**
     * @param submitTime
     */
    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    /**
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return way
     */
    public Integer getWay() {
        return way;
    }

    /**
     * @param way
     */
    public void setWay(Integer way) {
        this.way = way;
    }

    /**
     * @return state
     */
    public Integer getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return operator_id
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return operator_name
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * @return operate_time
     */
    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    /**
     * @param operateTime
     */
    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * @return account_number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return bank_name
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return bank_card_holder
     */
    public String getBankCardHolder() {
        return bankCardHolder;
    }

    /**
     * @param bankCardHolder
     */
    public void setBankCardHolder(String bankCardHolder) {
        this.bankCardHolder = bankCardHolder;
    }

    /**
     * @return serial_number
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDate getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(LocalDate chargeDate) {
        this.chargeDate = chargeDate;
    }

    public Long getTradeFundAccountId() {
        return tradeFundAccountId;
    }

    public void setTradeFundAccountId(Long tradeFundAccountId) {
        this.tradeFundAccountId = tradeFundAccountId;
    }

    public Long getTradeAccountId() {
        return tradeAccountId;
    }

    public void setTradeAccountId(Long tradeAccountId) {
        this.tradeAccountId = tradeAccountId;
    }

    public Long getTradeCustomerId() {
        return tradeCustomerId;
    }

    public void setTradeCustomerId(Long tradeCustomerId) {
        this.tradeCustomerId = tradeCustomerId;
    }

    public String getTradeCustomerName() {
        return tradeCustomerName;
    }

    public void setTradeCustomerName(String tradeCustomerName) {
        this.tradeCustomerName = tradeCustomerName;
    }

    public String getTradeCustomerCode() {
        return tradeCustomerCode;
    }

    public void setTradeCustomerCode(String tradeCustomerCode) {
        this.tradeCustomerCode = tradeCustomerCode;
    }

    public String getTradeCardNo() {
        return tradeCardNo;
    }

    public void setTradeCardNo(String tradeCardNo) {
        this.tradeCardNo = tradeCardNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Integer getReverse() {
        return reverse;
    }

    public void setReverse(Integer reverse) {
        this.reverse = reverse;
    }

    public Integer getDeductEnable() {
        return deductEnable;
    }

    public void setDeductEnable(Integer deductEnable) {
        this.deductEnable = deductEnable;
    }

    public String getTrailerNumber() {
        return trailerNumber;
    }

    public void setTrailerNumber(String trailerNumber) {
        this.trailerNumber = trailerNumber;
    }

    public String getHoldName() {
        return holdName;
    }

    public void setHoldName(String holdName) {
        this.holdName = holdName;
    }

    public String getHoldCertificateNumber() {
        return holdCertificateNumber;
    }

    public void setHoldCertificateNumber(String holdCertificateNumber) {
        this.holdCertificateNumber = holdCertificateNumber;
    }

    public String getHoldContactsPhone() {
        return holdContactsPhone;
    }

    public void setHoldContactsPhone(String holdContactsPhone) {
        this.holdContactsPhone = holdContactsPhone;
    }
}