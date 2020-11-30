package com.dili.settlement.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.ss.util.MoneyUtils;
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

    @Column(name = "`settle_order_id`")
    private Long settleOrderId;

    @Column(name = "`settle_order_code`")
    private String settleOrderCode;

    @Column(name = "`way`")
    private Integer way;

    @Column(name = "`amount`")
    private Long amount;

    @Column(name = "`serial_number`")
    private String serialNumber;

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
     * getter
     * @return
     */
    public Long getSettleOrderId() {
        return settleOrderId;
    }

    /**
     * setter
     * @param settleOrderId
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
     *
     * @return serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     *
     * @param serialNumber
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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
     *
     * @return
     */
    @Transient
    public String getWayName() {
        if (this.way == null) {
            return null;
        }
        return SettleWayEnum.getNameByCode(this.way);
    }
}