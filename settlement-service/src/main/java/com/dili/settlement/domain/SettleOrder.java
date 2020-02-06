package com.dili.settlement.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import java.time.LocalDateTime;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-02-05 16:39:11.
 */
@Table(name = "`settle_order`")
public class SettleOrder extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`app_id`")
    private Long appId;

    @Column(name = "`code`")
    private String code;

    @Column(name = "`business_code`")
    private String businessCode;

    @Column(name = "`business_type`")
    private Integer businessType;

    @Column(name = "`business_dep_id`")
    private Long businessDepId;

    @Column(name = "`business_dep_name`")
    private String businessDepName;

    @Column(name = "`customer_id`")
    private Long customerId;

    @Column(name = "`customer_name`")
    private String customerName;

    @Column(name = "`customer_phone`")
    private String customerPhone;

    @Column(name = "`amount`")
    private Long amount;

    @Column(name = "`submitter_id`")
    private Long submitterId;

    @Column(name = "`submitter_name`")
    private String submitterName;

    @Column(name = "`submitter_dep_id`")
    private Long submitterDepId;

    @Column(name = "`submitter_dep_name`")
    private String submitterDepName;

    @Column(name = "`submit_time`")
    private LocalDateTime submitTime;

    @Column(name = "`type`")
    private Integer type;

    @Column(name = "`way`")
    private Integer way;

    @Column(name = "`state`")
    private Integer state;

    @Column(name = "`operator_id`")
    private Long operatorId;

    @Column(name = "`operator_name`")
    private String operatorName;

    @Column(name = "`operate_time`")
    private LocalDateTime operateTime;

    @Column(name = "`account_number`")
    private String accountNumber;

    @Column(name = "`bank_name`")
    private String bankName;

    @Column(name = "`bank_card_holder`")
    private String bankCardHolder;

    @Column(name = "`serial_number`")
    private String serialNumber;

    @Column(name = "`edit_enable`")
    private Integer editEnable;

    @Column(name = "`notes`")
    private String notes;

    @Column(name = "`return_url`")
    private String returnUrl;

    @Column(name = "`version`")
    private Integer version;

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
    public Integer getBusinessType() {
        return businessType;
    }

    /**
     * @param businessType
     */
    public void setBusinessType(Integer businessType) {
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
     * @return edit_enable
     */
    @FieldDef(label="editEnable")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getEditEnable() {
        return editEnable;
    }

    /**
     * @param editEnable
     */
    public void setEditEnable(Integer editEnable) {
        this.editEnable = editEnable;
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

    /**
     * @return return_url
     */
    @FieldDef(label="returnUrl", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * @param returnUrl
     */
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}