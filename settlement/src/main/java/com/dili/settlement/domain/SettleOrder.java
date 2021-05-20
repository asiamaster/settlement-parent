package com.dili.settlement.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.settlement.annotation.DisplayConvert;
import com.dili.settlement.annotation.DisplayText;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.ss.util.MoneyUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-02-05 16:39:11.
 */
@DisplayConvert
@Table(name = "`settle_order`")
public class SettleOrder extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //市场ID
    @Column(name = "`market_id`")
    private Long marketId;

    //市场编码
    @Column(name = "`market_code`")
    private String marketCode;

    //商户ID
    @Column(name = "`mch_id`")
    private Long mchId;

    //商户名称
    @Column(name = "`mch_name`")
    private String mchName;

    //应用ID
    @Column(name = "`app_id`")
    private Long appId;

    //结算编号
    @Column(name = "`code`")
    private String code;

    //订单号  唯一
    @Column(name = "`order_code`")
    private String orderCode;

    //业务编号 具体业务单号
    @Column(name = "`business_code`")
    private String businessCode;

    //业务类型
    @Column(name = "`business_type`")
    @DisplayText(provider = "businessTypeProvider")
    private String businessType;

    //业务部门id
    @Column(name = "`business_dep_id`")
    private Long businessDepId;

    //业务部门名称
    @Column(name = "`business_dep_name`")
    private String businessDepName;

    //客户ID
    @Column(name = "`customer_id`")
    private Long customerId;

    //客户姓名
    @Column(name = "`customer_name`")
    private String customerName;

    //客户手机号
    @Column(name = "`customer_phone`")
    private String customerPhone;

    //客户证件号
    @Column(name = "`customer_certificate`")
    private String customerCertificate;

    //金额
    @Column(name = "`amount`")
    @DisplayText(provider = "moneyProvider")
    private Long amount;

    //提交人ID
    @Column(name = "`submitter_id`")
    private Long submitterId;

    //提交人姓名
    @Column(name = "`submitter_name`")
    private String submitterName;

    //提交人部门ID
    @Column(name = "`submitter_dep_id`")
    private Long submitterDepId;

    //提交人部门名称
    @Column(name = "`submitter_dep_name`")
    private String submitterDepName;

    //提交时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`submit_time`")
    private LocalDateTime submitTime;

    //结算类型 枚举 SettleTypeEnum
    @Column(name = "`type`")
    @DisplayText(provider = "settleTypeProvider")
    private Integer type;

    //结算方式 枚举 SettleWayEnum
    @Column(name = "`way`")
    @DisplayText(provider = "settleWayProvider")
    private Integer way;

    //结算状态 枚举 SettleStateEnum
    @Column(name = "`state`")
    @DisplayText(provider = "settleStateProvider")
    private Integer state;

    //结算员ID
    @Column(name = "`operator_id`")
    private Long operatorId;

    //结算员姓名
    @Column(name = "`operator_name`")
    private String operatorName;

    //结算时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`operate_time`")
    private LocalDateTime operateTime;

    //银行卡号
    @Column(name = "`account_number`")
    private String accountNumber;

    //银行名称
    @Column(name = "`bank_name`")
    private String bankName;

    //银行卡主
    @Column(name = "`bank_card_holder`")
    private String bankCardHolder;

    //流水号
    @Column(name = "`serial_number`")
    private String serialNumber;

    //备注
    @Column(name = "`notes`")
    private String notes;

    @Column(name = "`version`")
    private Integer version;

    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "`charge_date`")
    private LocalDate chargeDate;

    /** 交易资金账户ID */
    @Column(name = "`trade_fund_account_id`")
    private Long tradeFundAccountId;

    /** 交易卡账户ID */
    @Column(name = "`trade_account_id`")
    private Long tradeAccountId;

    /** 交易卡客户ID */
    @Column(name = "`trade_customer_id`")
    private Long tradeCustomerId;

    /** 交易卡客户姓名 */
    @Column(name = "`trade_customer_name`")
    private String tradeCustomerName;

    /**
     * 交易客户编号
     */
    @Column(name = "`trade_customer_code`")
    private String tradeCustomerCode;

    /** 交易卡号 */
    @Column(name = "`trade_card_no`")
    private String tradeCardNo;

    /** 交易流水号 */
    @Column(name = "`trade_no`")
    private String tradeNo;

    /** 是否冲正 0-否 1-是*/
    @Column(name = "`reverse`")
    @DisplayText(provider = "reverseProvider")
    private Integer reverse;

    /** 挂号(沈阳特有)*/
    @Column(name = "`trailer_number`")
    private String trailerNumber;

    /** 持卡人姓名 */
    @Column(name = "`hold_name`")
    private String holdName;
    /** 持卡人证件号 */
    @Column(name = "`hold_certificate_number`")
    private String holdCertificateNumber;
    /** 持卡人联系电话 */
    @Column(name = "`hold_contacts_phone`")
    private String holdContactsPhone;

    /** 扣减总额 */
    @Column(name = "`deduct_amount`")
    @DisplayText(provider = "moneyProvider")
    private Long deductAmount;

    /** 转抵总额 */
    @Column(name = "`transfer_amount`")
    @DisplayText(provider = "moneyProvider")
    private Long transferAmount;

    /**
     * @return id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
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
    @FieldDef(label="marketId")
    @EditMode(editor = FieldEditor.Number, required = false)
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
    @FieldDef(label="appId")
    @EditMode(editor = FieldEditor.Number, required = false)
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
    @FieldDef(label="code", maxLength = 32)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="businessCode", maxLength = 32)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="businessType")
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="businessDepId")
    @EditMode(editor = FieldEditor.Number, required = false)
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
    @FieldDef(label="businessDepName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="customerId")
    @EditMode(editor = FieldEditor.Number, required = false)
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
    @FieldDef(label="customerName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="customerPhone", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="amount")
    @EditMode(editor = FieldEditor.Number, required = false)
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
     * @return submitter_id
     */
    @FieldDef(label="submitterId")
    @EditMode(editor = FieldEditor.Number, required = false)
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
    @FieldDef(label="submitterName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="submitterDepId")
    @EditMode(editor = FieldEditor.Number, required = false)
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
    @FieldDef(label="submitterDepName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="submitTime")
    @EditMode(editor = FieldEditor.Datetime, required = false)
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
    @FieldDef(label="type")
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="way")
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="state")
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="operatorId")
    @EditMode(editor = FieldEditor.Number, required = false)
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
    @FieldDef(label="operatorName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="operateTime")
    @EditMode(editor = FieldEditor.Datetime, required = false)
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
    @FieldDef(label="accountNumber", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="bankName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="bankCardHolder", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="serialNumber", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="notes", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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

    public Long getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(Long deductAmount) {
        this.deductAmount = deductAmount;
    }

    public Long getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Long transferAmount) {
        this.transferAmount = transferAmount;
    }

    /**
     * 获取实付/实退金额
     * @return
     */
    public String getActualAmountText() {
        if (this.type == null || this.amount == null || this.deductAmount == null || this.transferAmount == null) {
            return null;
        }
        return this.type.equals(SettleTypeEnum.PAY.getCode()) ? MoneyUtils.centToYuan(this.amount - this.deductAmount) : MoneyUtils.centToYuan(this.amount - this.deductAmount - this.transferAmount);
    }
}