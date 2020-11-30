package com.dili.settlement.dto;

import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.domain.SettleOrderLink;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.enums.ReverseEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.ss.util.MoneyUtils;

import javax.persistence.Transient;
import java.util.List;

/**
 * 结算单DTO
 */
public class SettleOrderDto extends SettleOrder {

    //市场编码
    private String marketCode;
    //结算费用项列表
    private List<SettleFeeItem> settleFeeItemList;
    //结算单链接列表
    private List<SettleOrderLink> settleOrderLinkList;
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
    /** 支付单号或退款单号列表 */
    private List<String> orderCodeList;

    //是否进行枚举、字典值转换
    private Boolean convert;
    //业务名称 用businessType值进行转换
    private String businessName;
    //结算类型名称 用type值进行转换
    private String typeName;
    //结算方式名称 用way值进行转换
    private String wayName;
    //状态名称 用state值进行转换
    private String stateName;
    //重试记录ID
    private Long retryRecordId;
    //是否冲正标记 用reverse值进行转换
    private String reverseName;
    //结算方式明细
    private List<SettleWayDetail> settleWayDetailList;

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

    public List<String> getOrderCodeList() {
        return orderCodeList;
    }

    public void setOrderCodeList(List<String> orderCodeList) {
        this.orderCodeList = orderCodeList;
    }

    public Boolean getConvert() {
        return convert;
    }

    public void setConvert(Boolean convert) {
        this.convert = convert;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getWayName() {
        return wayName;
    }

    public void setWayName(String wayName) {
        this.wayName = wayName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Long getRetryRecordId() {
        return retryRecordId;
    }

    public void setRetryRecordId(Long retryRecordId) {
        this.retryRecordId = retryRecordId;
    }

    public String getReverseName() {
        return reverseName;
    }

    public void setReverseName(String reverseName) {
        this.reverseName = reverseName;
    }

    public List<SettleWayDetail> getSettleWayDetailList() {
        return settleWayDetailList;
    }

    public void setSettleWayDetailList(List<SettleWayDetail> settleWayDetailList) {
        this.settleWayDetailList = settleWayDetailList;
    }

    /**
     * 获取金额展示
     * @return
     */
    @Transient
    public String getAmountView() {
        if (this.getAmount() == null) {
            return null;
        }
        return MoneyUtils.centToYuan(this.getAmount());
    }

    /**
     * 是否可打印
     * @return
     */
    @Transient
    public boolean getPrintEnable() {
        return Integer.valueOf(SettleStateEnum.DEAL.getCode()).equals(this.getState()) && Integer.valueOf(ReverseEnum.NO.getCode()).equals(this.getReverse());
    }
}
