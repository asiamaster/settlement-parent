package com.dili.settlement.dto.pay;

/**
 * 撤销交易dto
 */
public class InvalidTradeRequestDto {

    /** 交易单号 */
    private String tradeId;

    public static InvalidTradeRequestDto build(String tradeId) {
        InvalidTradeRequestDto requestDto = new InvalidTradeRequestDto();
        requestDto.setTradeId(tradeId);
        return requestDto;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
}
