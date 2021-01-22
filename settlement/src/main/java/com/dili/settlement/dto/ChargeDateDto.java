package com.dili.settlement.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.settlement.domain.SettleWayDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 收款日期相关DTO
 */
public class ChargeDateDto {

    //结算单ID
    private Long id;
    //收款日期
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate chargeDate;
    //结算方式明细列表
    private List<SettleWayDetail> settleWayDetailList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(LocalDate chargeDate) {
        this.chargeDate = chargeDate;
    }

    public List<SettleWayDetail> getSettleWayDetailList() {
        return settleWayDetailList;
    }

    public void setSettleWayDetailList(List<SettleWayDetail> settleWayDetailList) {
        this.settleWayDetailList = settleWayDetailList;
    }
}
