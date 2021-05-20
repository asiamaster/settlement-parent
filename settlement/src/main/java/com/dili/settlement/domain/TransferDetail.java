package com.dili.settlement.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import javax.persistence.*;

/**
 * 转抵明细
 */
@Table(name = "`transfer_detail`")
public class TransferDetail extends BaseDomain {

    /**
     * 主键ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 结算单ID
     */
    @Column(name = "`settle_order_id`")
    private Long settleOrderId;

    /**
     * 结算单编码
     */
    @Column(name = "`settle_order_code`")
    private String settleOrderCode;

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

    /** 收费项ID */
    @Column(name = "`charge_item_id`")
    private Long chargeItemId;

    /** 收费项名称 */
    @Column(name = "`charge_item_name`")
    private String chargeItemName;

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
     * 获取结算单ID
     *
     * @return settle_order_id - 结算单ID
     */
    @FieldDef(label="结算单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
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
    @FieldDef(label="结算单编码", maxLength = 32)
    @EditMode(editor = FieldEditor.Text, required = false)
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
}