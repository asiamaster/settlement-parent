package com.dili.settlement.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 客户资金流水
 * This file was generated on 2020-12-03 18:15:40.
 */
@Table(name = "`customer_account_serial`")
public class CustomerAccountSerial extends BaseDomain {
    /**
     * 主键ID
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户资金ID
     */
    @Column(name = "`customer_account_id`")
    private Long customerAccountId;

    /**
     * 费用类型
     */
    @Column(name = "`fee_type`")
    private Integer feeType;

    /**
     * 资金动作
     */
    @Column(name = "`action`")
    private Integer action;

    /**
     * 场景 1-缴费...
     */
    @Column(name = "`scene`")
    private Integer scene;

    /**
     * 金额 正值表示收入 负值表示支出
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 时间
     */
    @Column(name = "`operate_time`")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    /**
     * 操作员ID
     */
    @Column(name = "`operator_id`")
    private Long operatorId;

    /**
     * 操作员姓名
     */
    @Column(name = "`operator_name`")
    private String operatorName;

    /**
     * 关联单号
     */
    @Column(name = "`relation_code`")
    private String relationCode;

    /**
     * 关联单号
     */
    @Column(name = "`relation_type`")
    private Integer relationType;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    private String notes;

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
     * 获取客户资金ID
     *
     * @return customer_account_id - 客户资金ID
     */
    @FieldDef(label="客户资金ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCustomerAccountId() {
        return customerAccountId;
    }

    /**
     * 设置客户资金ID
     *
     * @param customerAccountId 客户资金ID
     */
    public void setCustomerAccountId(Long customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    /**
     * getter
     * @return
     */
    public Integer getFeeType() {
        return feeType;
    }

    /**
     * setter
     * @param feeType
     */
    public void setFeeType(Integer feeType) {
        this.feeType = feeType;
    }

    /**
     * getter
     * @return
     */
    public Integer getAction() {
        return action;
    }

    /**
     * setter
     * @param action
     */
    public void setAction(Integer action) {
        this.action = action;
    }

    /**
     * 获取场景 1-缴费...
     *
     * @return scene - 场景 1-缴费...
     */
    @FieldDef(label="场景 1-缴费...")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getScene() {
        return scene;
    }

    /**
     * 设置场景 1-缴费...
     *
     * @param scene 场景 1-缴费...
     */
    public void setScene(Integer scene) {
        this.scene = scene;
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
     * 获取时间
     *
     * @return operate_time - 时间
     */
    @FieldDef(label="时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    /**
     * 设置时间
     *
     * @param operateTime 时间
     */
    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 获取操作员ID
     *
     * @return operator_id - 操作员ID
     */
    @FieldDef(label="操作员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * 设置操作员ID
     *
     * @param operatorId 操作员ID
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * 获取操作员姓名
     *
     * @return operator_name - 操作员姓名
     */
    @FieldDef(label="操作员姓名", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * 设置操作员姓名
     *
     * @param operatorName 操作员姓名
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * 获取关联单号
     *
     * @return relation_code - 关联单号
     */
    @FieldDef(label="关联单号", maxLength = 32)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getRelationCode() {
        return relationCode;
    }

    /**
     * 设置关联单号
     *
     * @param relationCode 关联单号
     */
    public void setRelationCode(String relationCode) {
        this.relationCode = relationCode;
    }

    /**
     * 获取关联单号
     *
     * @return relation_type - 关联单号
     */
    @FieldDef(label="关联单号", maxLength = 32)
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getRelationType() {
        return relationType;
    }

    /**
     * 设置关联单号
     *
     * @param relationType 关联单号
     */
    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    /**
     * 获取备注
     *
     * @return notes - 备注
     */
    @FieldDef(label="备注", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    /**
     * 设置备注
     *
     * @param notes 备注
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static CustomerAccountSerial build(Integer feeType, Integer action, Integer scene, Long amount, LocalDateTime operateTime, Long operatorId, String operatorName, String relationCode, Integer relationType, String notes) {
        CustomerAccountSerial accountSerial = new CustomerAccountSerial();
        accountSerial.setFeeType(feeType);
        accountSerial.setAction(action);
        accountSerial.setScene(scene);
        accountSerial.setAmount(amount);
        accountSerial.setOperateTime(operateTime);
        accountSerial.setOperatorId(operatorId);
        accountSerial.setOperatorName(operatorName);
        accountSerial.setRelationCode(relationCode);
        accountSerial.setRelationType(relationType);
        accountSerial.setNotes(notes);
        return accountSerial;
    }
}