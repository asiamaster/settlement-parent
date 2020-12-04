package com.dili.settlement.dto;

/**
 * 结算金额相关dto
 */
public class SettleAmountDto {

    //结算金额
    private Long totalAmount;
    //抵扣金额
    private Long totalDeductAmount;

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalDeductAmount() {
        return totalDeductAmount;
    }

    public void setTotalDeductAmount(Long totalDeductAmount) {
        this.totalDeductAmount = totalDeductAmount;
    }
}
