package com.dili.settlement.dto;

/**
 * 结算金额相关dto
 */
public class SettleAmountDto {

    //结算金额
    private Long totalAmount;
    //抵扣金额
    private Long totalDeductAmount;
    //总转抵金额
    private Long totalTransferAmount;

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

    public Long getTotalTransferAmount() {
        return totalTransferAmount;
    }

    public void setTotalTransferAmount(Long totalTransferAmount) {
        this.totalTransferAmount = totalTransferAmount;
    }

    /**
     * 计算实际支付金额
     * @return
     */
    public Long countPaySettleAmount() {
        return this.totalAmount - this.totalDeductAmount;
    }

    /**
     * 计算实际退款金额
     * @return
     */
    public Long countRefundSettleAmount() {
        return this.totalAmount - this.totalDeductAmount - this.totalTransferAmount;
    }
}
