package com.dili.settlement.dto;

import com.dili.settlement.domain.*;

import java.util.List;

/**
 * 结算单DTO
 */
public class SettleOrderDto extends SettleOrder {

    /** 提交 start */
    //结算费用项列表
    private List<SettleFeeItem> settleFeeItemList;
    //结算单链接列表
    private List<SettleOrderLink> settleOrderLinkList;
    //抵扣项列表
    private List<SettleFeeItem> deductFeeItemList;
    //转抵明细列表
    private List<TransferDetail> transferDetailList;
    //结算方式明细
    private List<SettleWayDetail> settleWayDetailList;
    /** 提交 end */

    /** 查询 start */
    //appId 列表
    private List<Long> appIdList;
    //客户名称模糊匹配
    private String customerNameMatch;
    //提交人模糊查询
    private String submitterNameMatch;
    //结算员模糊匹配
    private String operatorNameMatch;
    //结算开始时间
    private String operateTimeStart;
    //结算结束时间
    private String operateTimeEnd;
    //收款日期开始
    private String chargeDateStart;
    //收款日期结束
    private String chargeDateEnd;
    //支付单号或退款单号列表
    private List<String> orderCodeList;
    //业务类型查询列表
    private String[] businessTypeArray;
    /** 查询 end */

    /** 结算 start */
    //id串
    private String ids;
    //id列表
    private List<Long> idList;
    //交易密码
    private String tradePassword;
    //建议校验token
    private String token;
    //操作员工号
    private String operatorNo;
    //交易客户类型
    private String tradeCustomerType;
    //结算金额信息
    private SettleAmountDto settleAmountDto;
    /** 结算 end */

    public List<SettleFeeItem> getSettleFeeItemList() {
        return settleFeeItemList;
    }

    public void setSettleFeeItemList(List<SettleFeeItem> settleFeeItemList) {
        this.settleFeeItemList = settleFeeItemList;
    }

    public List<SettleOrderLink> getSettleOrderLinkList() {
        return settleOrderLinkList;
    }

    public void setSettleOrderLinkList(List<SettleOrderLink> settleOrderLinkList) {
        this.settleOrderLinkList = settleOrderLinkList;
    }

    public List<SettleWayDetail> getSettleWayDetailList() {
        return settleWayDetailList;
    }

    public List<SettleFeeItem> getDeductFeeItemList() {
        return deductFeeItemList;
    }

    public void setDeductFeeItemList(List<SettleFeeItem> deductFeeItemList) {
        this.deductFeeItemList = deductFeeItemList;
    }

    public List<TransferDetail> getTransferDetailList() {
        return transferDetailList;
    }

    public void setTransferDetailList(List<TransferDetail> transferDetailList) {
        this.transferDetailList = transferDetailList;
    }

    public void setSettleWayDetailList(List<SettleWayDetail> settleWayDetailList) {
        this.settleWayDetailList = settleWayDetailList;
    }

    public List<Long> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<Long> appIdList) {
        this.appIdList = appIdList;
    }

    public String getCustomerNameMatch() {
        return customerNameMatch;
    }

    public void setCustomerNameMatch(String customerNameMatch) {
        this.customerNameMatch = customerNameMatch;
    }

    public String getSubmitterNameMatch() {
        return submitterNameMatch;
    }

    public void setSubmitterNameMatch(String submitterNameMatch) {
        this.submitterNameMatch = submitterNameMatch;
    }

    public String getOperatorNameMatch() {
        return operatorNameMatch;
    }

    public void setOperatorNameMatch(String operatorNameMatch) {
        this.operatorNameMatch = operatorNameMatch;
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

    public List<String> getOrderCodeList() {
        return orderCodeList;
    }

    public void setOrderCodeList(List<String> orderCodeList) {
        this.orderCodeList = orderCodeList;
    }

    public String[] getBusinessTypeArray() {
        return businessTypeArray;
    }

    public void setBusinessTypeArray(String[] businessTypeArray) {
        this.businessTypeArray = businessTypeArray;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public String getTradePassword() {
        return tradePassword;
    }

    public void setTradePassword(String tradePassword) {
        this.tradePassword = tradePassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public String getTradeCustomerType() {
        return tradeCustomerType;
    }

    public void setTradeCustomerType(String tradeCustomerType) {
        this.tradeCustomerType = tradeCustomerType;
    }

    public SettleAmountDto getSettleAmountDto() {
        return settleAmountDto;
    }

    public void setSettleAmountDto(SettleAmountDto settleAmountDto) {
        this.settleAmountDto = settleAmountDto;
    }
}
