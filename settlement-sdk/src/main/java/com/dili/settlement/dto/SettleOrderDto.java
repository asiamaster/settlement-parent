package com.dili.settlement.dto;

import com.dili.settlement.domain.SettleOrder;

import java.util.List;

/**
 * 结算单DTO
 */
public class SettleOrderDto extends SettleOrder {

    //市场编码
    private String marketCode;
    //id列表
    private List<Long> idList;
    //业务类型查询列表
    private List<Integer> businessTypeList;
    //结算开始时间
    private String operateTimeStart;
    //结算结束时间
    private String operateTimeEnd;
    //id串
    private String ids;
    //客户名称模糊匹配
    private String customerNameMatch;
    //结算员模糊匹配
    private String operatorNameMatch;
    //辅助token
    private String token;
    //appId 列表
    private List<Long> appIdList;
    //收款日期开始
    private String chargeDateStart;
    //收款日期结束
    private String chargeDateEnd;
    //收款总额
    private Long totalAmount;
    /** 交易密码 */
    private String tradePassword;

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public List<Integer> getBusinessTypeList() {
        return businessTypeList;
    }

    public void setBusinessTypeList(List<Integer> businessTypeList) {
        this.businessTypeList = businessTypeList;
    }

    public String getOperateTimeStart() {
        return operateTimeStart;
    }

    public void setOperateTimeStart(String operateTimeStart) {
        this.operateTimeStart = operateTimeStart;
    }

    public String getOperateTimeEnd() {
        return operateTimeEnd;
    }

    public void setOperateTimeEnd(String operateTimeEnd) {
        this.operateTimeEnd = operateTimeEnd;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getCustomerNameMatch() {
        return customerNameMatch;
    }

    public void setCustomerNameMatch(String customerNameMatch) {
        this.customerNameMatch = customerNameMatch;
    }

    public String getOperatorNameMatch() {
        return operatorNameMatch;
    }

    public void setOperatorNameMatch(String operatorNameMatch) {
        this.operatorNameMatch = operatorNameMatch;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Long> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<Long> appIdList) {
        this.appIdList = appIdList;
    }

    public String getChargeDateStart() {
        return chargeDateStart;
    }

    public void setChargeDateStart(String chargeDateStart) {
        this.chargeDateStart = chargeDateStart;
    }

    public String getChargeDateEnd() {
        return chargeDateEnd;
    }

    public void setChargeDateEnd(String chargeDateEnd) {
        this.chargeDateEnd = chargeDateEnd;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTradePassword() {
        return tradePassword;
    }

    public void setTradePassword(String tradePassword) {
        this.tradePassword = tradePassword;
    }
}
