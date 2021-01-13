package com.dili.settlement.dto;

import java.util.List;

/**
 * 用于作废请求参数dto
 */
public class InvalidRequestDto {

    /** 市场ID */
    private Long marketId;
    /** 市场编码 */
    private String marketCode;
    /** 应用ID */
    private Long appId;
    /** 操作员ID */
    private Long operatorId;
    /** 操作员姓名 */
    private String operatorName;
    /** 操作员工号 */
    private String operatorNo;
    /** 支付单号或退款单号列表 */
    private List<String> orderCodeList;

    /**
     * getter
     * @return
     */
    public Long getMarketId() {
        return marketId;
    }

    /**
     * setter
     * @param marketId
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * getter
     * @return
     */
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * setter
     * @param marketCode
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    /**
     * getter
     * @return
     */
    public Long getAppId() {
        return appId;
    }

    /**
     * setter
     * @param appId
     */
    public void setAppId(Long appId) {
        this.appId = appId;
    }

    /**
     * getter
     * @return
     */
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * setter
     * @param operatorId
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * getter
     * @return
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * setter
     * @param operatorName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * getter
     * @return
     */
    public String getOperatorNo() {
        return operatorNo;
    }

    /**
     * setter
     * @param operatorNo
     */
    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    /**
     * getter
     * @return
     */
    public List<String> getOrderCodeList() {
        return orderCodeList;
    }

    /**
     * setter
     * @param orderCodeList
     */
    public void setOrderCodeList(List<String> orderCodeList) {
        this.orderCodeList = orderCodeList;
    }
}
