package com.dili.settlement.dto;

import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.dto.pay.FeeItemDto;

import java.util.List;

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
    //定金金额
    private Long earnestAmount;
    //定金账户流水
    private List<CustomerAccountSerial> accountSerialList;

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

    public Long getEarnestAmount() {
        return earnestAmount;
    }

    public void setEarnestAmount(Long earnestAmount) {
        this.earnestAmount = earnestAmount;
    }

    public List<CustomerAccountSerial> getAccountSerialList() {
        return accountSerialList;
    }

    public void setAccountSerialList(List<CustomerAccountSerial> accountSerialList) {
        this.accountSerialList = accountSerialList;
    }
}
