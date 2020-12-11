package com.dili.settlement.dto.pay;

import java.util.List;

/**
 * 退款交易请求参数dto
 * @author xuliang
 */
public class RefundRequestDto {

    /** 交易号*/
    private String tradeId;
    /** 退款金额*/
    private Long amount;
    /** 费用项*/
    private List<FeeItemDto> fees;
    /** 抵扣费用项*/
    private List<FeeItemDto> deductFees;

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public List<FeeItemDto> getFees() {
        return fees;
    }

    public void setFees(List<FeeItemDto> fees) {
        this.fees = fees;
    }

    public List<FeeItemDto> getDeductFees() {
        return deductFees;
    }

    public void setDeductFees(List<FeeItemDto> deductFees) {
        this.deductFees = deductFees;
    }

    /**
     * 创建提交交易参数
     * @return
     */
    public static RefundRequestDto build(String tradeId, Long amount, List<FeeItemDto> fees, List<FeeItemDto> deductFees) {
        RefundRequestDto refundRequestDto = new RefundRequestDto();
        refundRequestDto.setTradeId(tradeId);
        refundRequestDto.setAmount(amount);
        refundRequestDto.setFees(fees);
        refundRequestDto.setDeductFees(deductFees);
        return refundRequestDto;
    }
}
