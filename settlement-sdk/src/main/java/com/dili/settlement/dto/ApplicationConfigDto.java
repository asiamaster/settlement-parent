package com.dili.settlement.dto;

import com.dili.settlement.domain.ApplicationConfig;

import java.util.List;

/**
 * 应用配置dto
 */
public class ApplicationConfigDto extends ApplicationConfig {

    //业务类型
    private Integer businessType;
    //业务编码
    private String businessCode;
    //是否补打  1 否  2  是
    private Integer reprint;
    //appId 列表
    private List<Long> appIdList;
    private String appIds;

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
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
}
