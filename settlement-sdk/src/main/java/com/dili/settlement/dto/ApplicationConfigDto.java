package com.dili.settlement.dto;

import com.dili.settlement.domain.ApplicationConfig;

import java.util.List;

/**
 * 应用配置dto
 */
public class ApplicationConfigDto extends ApplicationConfig {

    //结算类型
    private Integer settleType;
    //业务类型
    private Integer businessType;
    //订单号
    private String orderCode;
    //业务编码
    private String businessCode;
    //是否补打  1 否  2  是
    private Integer reprint;
    //appId 列表
    private List<Long> appIdList;
    private String appIds;
    //红冲标记
    private Integer reverse;

    public Integer getSettleType() {
        return settleType;
    }

    public void setSettleType(Integer settleType) {
        this.settleType = settleType;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public Integer getReprint() {
        return reprint;
    }

    public void setReprint(Integer reprint) {
        this.reprint = reprint;
    }

    public List<Long> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<Long> appIdList) {
        this.appIdList = appIdList;
    }

    public String getAppIds() {
        return appIds;
    }

    public void setAppIds(String appIds) {
        this.appIds = appIds;
    }

    public Integer getReverse() {
        return reverse;
    }

    public void setReverse(Integer reverse) {
        this.reverse = reverse;
    }
}
