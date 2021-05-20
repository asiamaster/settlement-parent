package com.dili.settlement.dto;

import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.dto.pay.FeeItemDto;

import java.util.List;
import java.util.Map;

/**
 * 结算数据准备dto
 */
public class SettleDataDto {

    //重试记录列表
    private List<RetryRecord> retryRecordList;
    //支付费用项列表
    private List<FeeItemDto> feeItemList;
    //抵扣项费用项列表
    private List<FeeItemDto> deductFeeItemList;
    //定金账户变动
    private Map<Long, AccountMergeDto> earnestMap;
    //转抵账户变动
    private Map<Long, AccountMergeDto> transferMap;

    public List<RetryRecord> getRetryRecordList() {
        return retryRecordList;
    }

    public void setRetryRecordList(List<RetryRecord> retryRecordList) {
        this.retryRecordList = retryRecordList;
    }

    public List<FeeItemDto> getFeeItemList() {
        return feeItemList;
    }

    public void setFeeItemList(List<FeeItemDto> feeItemList) {
        this.feeItemList = feeItemList;
    }

    public List<FeeItemDto> getDeductFeeItemList() {
        return deductFeeItemList;
    }

    public void setDeductFeeItemList(List<FeeItemDto> deductFeeItemList) {
        this.deductFeeItemList = deductFeeItemList;
    }

    public Map<Long, AccountMergeDto> getEarnestMap() {
        return earnestMap;
    }

    public void setEarnestMap(Map<Long, AccountMergeDto> earnestMap) {
        this.earnestMap = earnestMap;
    }

    public Map<Long, AccountMergeDto> getTransferMap() {
        return transferMap;
    }

    public void setTransferMap(Map<Long, AccountMergeDto> transferMap) {
        this.transferMap = transferMap;
    }
}
