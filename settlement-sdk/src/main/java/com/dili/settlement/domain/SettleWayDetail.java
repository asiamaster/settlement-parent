package com.dili.settlement.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-06-01 14:13:21.
 */
@Table(name = "`settle_way_detail`")
public class SettleWayDetail extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`order_id`")
    private Long orderId;

    @Column(name = "`order_code`")
    private String orderCode;

    @Column(name = "`way`")
    private Byte way;

    @Column(name = "`amount`")
    private Long amount;

    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "`charge_date`")
    private LocalDate chargeDate;

    @Column(name = "`notes`")
    private String notes;

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
     * @return order_id
     */
    @FieldDef(label="orderId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * @return order_code
     */
    @FieldDef(label="orderCode", maxLength = 32)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * @param orderCode
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * @return way
     */
    @FieldDef(label="way")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Byte getWay() {
        return way;
    }

    /**
     * @param way
     */
    public void setWay(Byte way) {
        this.way = way;
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
     * @return charge_date
     */
    @FieldDef(label="chargeDate")
    @EditMode(editor = FieldEditor.Date, required = false)
    public LocalDate getChargeDate() {
        return chargeDate;
    }

    /**
     * @param chargeDate
     */
    public void setChargeDate(LocalDate chargeDate) {
        this.chargeDate = chargeDate;
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
}