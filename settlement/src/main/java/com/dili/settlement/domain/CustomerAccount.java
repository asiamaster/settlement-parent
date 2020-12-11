package com.dili.settlement.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 客户资金表
 * This file was generated on 2020-12-01 16:37:13.
 */
@Table(name = "`customer_account`")
public class CustomerAccount extends BaseDomain {
    /**
     * 主键ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 市场ID
     */
    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 市场code
     */
    @Column(name = "`market_code`")
    private String marketCode;

    //商户ID
    @Column(name = "`mch_id`")
    private Long mchId;

    //商户名称
    @Column(name = "`mch_name`")
    private String mchName;

    /**
     * 客户ID
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     * 客户姓名
     */
    @Column(name = "`customer_name`")
    private String customerName;

    /**
     * 客户手机号
     */
    @Column(name = "`customer_phone`")
    private String customerPhone;

    /**
     * 客户证件号
     */
    @Column(name = "`customer_certificate`")
    private String customerCertificate;

    /**
     * 金额
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 冻结金额
     */
    @Column(name = "`frozen_amount`")
    private Long frozenAmount;

    @Column(name = "`version`")
    private Integer version;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    @FieldDef(label="主键ID")
    @EditMode(editor = FieldEditor.Number, required = true)
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
     * 获取市场ID
     *
     * @return market_id - 市场ID
     */
    @FieldDef(label="市场ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * 设置市场ID
     *
     * @param marketId 市场ID
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
     * 获取客户ID
     *
     * @return customer_id - 客户ID
     */
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
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
    @FieldDef(label="客户姓名", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="客户手机号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="客户证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
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
    @FieldDef(label="金额")
    @EditMode(editor = FieldEditor.Number, required = false)
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

    /**
     * 获取冻结金额
     *
     * @return frozen_amount - 冻结金额
     */
    @FieldDef(label="冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getFrozenAmount() {
        return frozenAmount;
    }

    /**
     * 设置冻结金额
     *
     * @param frozenAmount 冻结金额
     */
    public void setFrozenAmount(Long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    /**
     * @return version
     */
    @FieldDef(label="version")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    public static CustomerAccount build(Long marketId, String marketCode, Long mchId, String mchName, Long customerId, String customerName, String customerPhone, String customerCertificate) {
        CustomerAccount account = new CustomerAccount();
        account.setMarketId(marketId);
        account.setMarketCode(marketCode);
        account.setMchId(mchId);
        account.setMchName(mchName);
        account.setCustomerId(customerId);
        account.setCustomerName(customerName);
        account.setCustomerPhone(customerPhone);
        account.setCustomerCertificate(customerCertificate);
        account.setAmount(0L);
        account.setFrozenAmount(0L);
        return account;
    }
}