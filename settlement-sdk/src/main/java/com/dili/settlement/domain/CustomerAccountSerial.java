package com.dili.settlement.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 客户资金流水
 * This file was generated on 2020-12-03 18:15:40.
 */
public class CustomerAccountSerial extends BaseDomain {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户资金ID
     */
    private Long customerAccountId;

    /**
     * 费用类型 201-定金 203-转抵
     */
    private Integer feeType;
    /**
     * 资金动作 ActionEnum
     */
    private Integer action;

    /**
     * 场景 1-缴费...
     */
    private Integer scene;

    /**
     * 金额 正值表示收入 负值表示支出
     */
    private Long amount;

    /**
     * 时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    /**
     * 操作员ID
     */
    private Long operatorId;

    /**
     * 操作员姓名
     */
    private String operatorName;

    /**
     * 关联单号
     */
    private String relationCode;

    /**
     * 关联单号
     */
    private Integer relationType;

    /**
     * 备注
     */
    private String notes;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
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

    public static CustomerAccountSerial build(Integer action, Integer scene, Long amount, LocalDateTime operateTime, Long operatorId, String operatorName, String relationCode, Integer relationType, String notes) {
        CustomerAccountSerial accountSerial = new CustomerAccountSerial();
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