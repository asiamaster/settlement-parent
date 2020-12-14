package com.dili.settlement.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-06-01 14:13:21.
 */
public class SettleWayDetail extends BaseDomain {

    private Long id;
    /**
     * 结算单ID
     */
    private Long settleOrderId;
    /**
     * 结算单CODE
     */
    private String settleOrderCode;
    /**
     * 结算方式 SettleWayEnum
     */
    private Integer way;
    /**
     * 金额
     */
    private Long amount;
    /**
     * 流水号
     */
    private String serialNumber;

    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate chargeDate;
    /**
     * 备注
     */
    private String notes;

    /**
     * @return id
     */
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
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static SettleWayDetail build(Long settleOrderId, String settleOrderCode, SettleWayDetail temp) {
        SettleWayDetail settleWayDetail = new SettleWayDetail();
        settleWayDetail.setSettleOrderId(settleOrderId);
        settleWayDetail.setSettleOrderCode(settleOrderCode);
        settleWayDetail.setWay(temp.getWay());
        settleWayDetail.setAmount(temp.getAmount());
        settleWayDetail.setSerialNumber(temp.getSerialNumber());
        settleWayDetail.setChargeDate(temp.getChargeDate());
        settleWayDetail.setNotes(temp.getNotes());
        return settleWayDetail;
    }

}