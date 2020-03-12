package com.dili.settlement.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.ss.util.MoneyUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    //市场ID
    @Column(name = "`market_id`")
    private Long marketId;

    //应用ID
    @Column(name = "`app_id`")
    private Long appId;

    //结算编号
    @Column(name = "`code`")
    private String code;

    //业务编号
    @Column(name = "`business_code`")
    private String businessCode;

    //业务类型
    @Column(name = "`business_type`")
    private Integer businessType;

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

    //金额
    @Column(name = "`amount`")
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
    private Integer type;

    //结算方式 枚举 SettleWayEnum
    @Column(name = "`way`")
    private Integer way;

    //结算状态 枚举 SettleStateEnum
    @Column(name = "`state`")
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

    //退款是否可编辑 枚举 EditEnableEnum
    @Column(name = "`edit_enable`")
    private Integer editEnable;

    //备注
    @Column(name = "`notes`")
    private String notes;

    //结算回调url
    @Column(name = "`return_url`")
    private String returnUrl;

    @Column(name = "`version`")
    private Integer version;

    //是否进行枚举、字典值转换
    @Transient
    private Boolean convert;
    //业务名称 用businessType值进行转换
    @Transient
    private String businessName;
    //结算类型名称 用type值进行转换
    @Transient
    private String typeName;
    //结算方式名称 用way值进行转换
    @Transient
    private String wayName;
    //状态名称 用state值进行转换
    @Transient
    private String stateName;
    //重试记录ID
    @Transient
    private Long retryRecordId;
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

    public Boolean getConvert() {
        return convert;
    }

    public void setConvert(Boolean convert) {
        this.convert = convert;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getWayName() {
        return wayName;
    }

    public void setWayName(String wayName) {
        this.wayName = wayName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Long getRetryRecordId() {
        return retryRecordId;
    }

    public void setRetryRecordId(Long retryRecordId) {
        this.retryRecordId = retryRecordId;
    }

    /**
     * 获取金额展示
     * @return
     */
    @Transient
    public String getAmountView() {
        if (this.amount == null) {
            return null;
        }
        return MoneyUtils.centToYuan(this.amount);
    }

    /**
     * 是否可打印
     * @return
     */
    @Transient
    public boolean getPrintEnable() {
        if (this.state == null) {
            return false;
        }
        return this.state.equals(SettleStateEnum.DEAL.getCode());
    }
}