package com.dili.settlement.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 结算费用项
 * This file was generated on 2020-11-26 16:30:28.
 */
@Table(name = "`settle_fee_item`")
public class SettleFeeItem extends BaseDomain {
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
     * 费用类型
     */
    @Column(name = "`fee_type`")
    private Integer feeType;

    /**
     * 费用名称
     */
    @Column(name = "`fee_name`")
    private String feeName;

    /**
     * 金额
     */
    @Column(name = "`amount`")
    private Long amount;

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
     * 获取费用类型
     *
     * @return fee_type - 费用类型
     */
    @FieldDef(label="费用类型")
    @EditMode(editor = FieldEditor.Number, required = false)
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
    @FieldDef(label="费用名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
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
}