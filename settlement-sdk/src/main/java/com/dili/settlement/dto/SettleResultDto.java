package com.dili.settlement.dto;

import com.dili.settlement.domain.SettleOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * 结算结果传输对象
 */
public class SettleResultDto {

    //总数
    private Integer totalNum;
    //成功数
    private Integer successNum = 0;
    //失败数
    private Integer failureNum = 0;
    //成功金额
    private Long successAmount = 0L;
    //失败金额
    private Long failureAmount = 0L;
    //成功单据记录
    private List<SettleOrder> successItemList = new ArrayList<>();
    //失败单据记录
    private List<SettleOrder>  failureItemList = new ArrayList<>();

    /**
     * 执行成功
     * @param settleOrder
     */
    public void success(SettleOrder settleOrder) {
        this.successNum += 1;
        if (settleOrder == null) {
            return;
        }
        this.successAmount += settleOrder.getAmount();
        this.successItemList.add(settleOrder);
    }

    /**
     * 执行失败 记录存在
     * @param settleOrder
     */
    public void failure(SettleOrder settleOrder) {
        this.failureNum += 1;
        if (settleOrder == null){
            return;
        }
        this.failureAmount += settleOrder.getAmount();
        this.failureItemList.add(settleOrder);
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getFailureNum() {
        return failureNum;
    }

    public void setFailureNum(Integer failureNum) {
        this.failureNum = failureNum;
    }

    public Long getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(Long successAmount) {
        this.successAmount = successAmount;
    }

    public Long getFailureAmount() {
        return failureAmount;
    }

    public void setFailureAmount(Long failureAmount) {
        this.failureAmount = failureAmount;
    }

    public List<SettleOrder> getSuccessItemList() {
        return successItemList;
    }

    public void setSuccessItemList(List<SettleOrder> successItemList) {
        this.successItemList = successItemList;
    }

    public List<SettleOrder> getFailureItemList() {
        return failureItemList;
    }

    public void setFailureItemList(List<SettleOrder> failureItemList) {
        this.failureItemList = failureItemList;
    }
}
